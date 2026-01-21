package com.simon.data.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.SocketFactory

class ConnectivityImpl(
    context: Context
) : Connectivity {

        private val _status = MutableStateFlow(Status.Initial)
        val status: StateFlow<Status> = _status.asStateFlow()

        private val manager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        private val scope: CoroutineScope = MainScope()

        private suspend fun checkInternet(): Boolean = withContext(Dispatchers.IO) {
            val dnsHosts = listOf("8.8.8.8", "1.1.1.1", "9.9.9.9")
            val httpHosts = listOf("https://www.google.com", "https://www.facebook.com")
            val timeout = 1500

            // Check DNS connectivity
            dnsHosts.forEach { host ->
                try {
                    SocketFactory.getDefault().createSocket()?.use {
                        it.connect(InetSocketAddress(host, 53), timeout)
                        return@withContext true // DNS connection successful
                    }
                } catch (e: IOException) {
                    // Ignore and try the next host
                }
            }

            // Check HTTP connectivity if DNS check fails
            httpHosts.forEach { host ->
                try {
                    val url = URL(host)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = timeout
                    connection.readTimeout = timeout
                    connection.requestMethod = "HEAD" // Only get headers
                    val responseCode = connection.responseCode
                    if (responseCode in 200..399) {
                        return@withContext true // HTTP request successful
                    }
                } catch (e: IOException) {
                    // Ignore and try the next host
                } catch (e: SocketTimeoutException) {

                }
            }

            return@withContext false // No connection could be established
        }

        private fun log(message: String) {
            Log.e("TAG", message)
        }

        private val callback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                val manager = manager
                if (manager == null) {
                    log("onAvailable(): ConnectivityManager is null! Aborted the operation.")
                    return
                }

                scope.launch {
                    if (manager.getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                        val hasInternet = checkInternet()
                        _status.emit(if (hasInternet) Status.Connected else Status.NotConnected)
                    } else {
                        _status.emit(Status.NotConnected)
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)

                scope.launch {
                    _status.emit(Status.NotConnected)
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        fun check(listener: (connected: Boolean) -> Unit) {
            scope.launch {
                listener(check())
            }
        }

        private suspend fun check(): Boolean {
            return checkInternet()
        }


        @RequiresApi(Build.VERSION_CODES.N)
        fun startObservingConnection() {
            val manager = manager
            if (manager == null) {
                log("startObservingConnection(): ConnectivityManager is null! Aborted the operation.")
                return
            }

            scope.launch {
                if (status.value != Status.Initial) return@launch
                _status.emit(if (check()) Status.Connected else Status.NotConnected)
                manager.registerDefaultNetworkCallback(callback)
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun stopObservingConnection() {
            val manager = manager
            if (manager == null) {
                log("stopObservingConnection(): ConnectivityManager is null! Aborted the operation.")
                return
            }

            scope.launch {
                manager.unregisterNetworkCallback(callback)
                _status.emit(Status.Initial)
            }
        }

        ///////////////////////////////////////////////////////////////////////////
        // MISC
        ///////////////////////////////////////////////////////////////////////////

        enum class Status {
            Initial,
            Connected,
            NotConnected;
        }

        override suspend fun isNetworkAvailable():Boolean {
            return checkInternet()
        }
    }
