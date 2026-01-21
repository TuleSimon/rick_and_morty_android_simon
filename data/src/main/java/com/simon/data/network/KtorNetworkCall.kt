package com.simon.data.network

import android.content.Context
import com.simon.data.logger.AppLogger
import com.simon.data.network.base.CallParams
import com.simon.data.network.base.NetworkCall
import com.simon.data.network.connectivity.Connectivity
import com.simon.domain.base.BaseResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.net.UnknownHostException

object ConstantErrorStrings {
    const val POOR_NETWORK = "poor_network"
    const val NO_INTERNET = "no_internet"
    const val NETWORK_ERROR = "network_error"
    const val CANCELLED_ERROR = "cancelled"
    const val UNKNOWN_ERROR = "unknown"
}

class KtorNetworkCall(
    private val context: Context,
    private val ktorClient: HttpClient,
    private val connectivity: Connectivity
) : NetworkCall {

    override suspend fun <T> get(
        route: String,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): BaseResult<T> {
        try {
            if (callParams.checkNetwork) {
                val isNetworkAvailable = connectivity.isNetworkAvailable()
                if (!isNetworkAvailable) return BaseResult.NetworkError
            }
            val response = ktorClient.get(route) {
                url {
                    queryParams.forEach {
                        parameters.append(it.key, it.value)
                    }
                    headerParams.forEach {
                        headers.append(it.key, it.value)
                    }
                    if (callParams.useCache.not()) {
                        headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                    }

                }
            }
            val body = response.body<T>(type)
            return BaseResult.Success(body)
        } catch (e: Exception) {
            return handleException(e)
        }
    }

    override suspend fun <T, R> delete(
        route: String,
        type: TypeInfo,
        body: R?,
        bodytype: TypeInfo?,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): BaseResult<T> {
        try {
            if (callParams.checkNetwork) {
                val isNetworkAvailable = connectivity.isNetworkAvailable()
                if (!isNetworkAvailable) return BaseResult.NetworkError
            }
            val response = ktorClient.delete(route) {
                contentType(ContentType.Application.Json)
                if (body != null && bodytype != null) {
                    setBody(body, bodytype)
                }
                url {
                    queryParams.forEach {
                        parameters.append(it.key, it.value)
                    }
                    headerParams.forEach {
                        headers.append(it.key, it.value)
                    }
                    if (callParams.useCache.not()) {
                        headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                    }

                }
            }
            val body = response.body<T>(type)
            return BaseResult.Success(body)
        } catch (e: Exception) {
            return handleException(e)
        }
    }

    override suspend fun <T> getFlow(
        route: String,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): Flow<BaseResult<T>> = flow {

        if (callParams.checkNetwork) {
            val isNetworkAvailable = connectivity.isNetworkAvailable()
            if (!isNetworkAvailable) emit(BaseResult.NetworkError)
        }
        val response = ktorClient.get(route) {
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value)
                }
                headerParams.forEach {
                    headers.append(it.key, it.value)
                }
                if (callParams.useCache.not()) {
                    headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                }

            }
        }
        val body = response.body<T>(type)
        emit(BaseResult.Success(body))

    }.onStart {
        emit(BaseResult.Loading())
    }.catch {
        emit(handleException(it))
    }

    override suspend fun <T, R> deleteFlow(
        route: String,
        type: TypeInfo,
        body: R?,
        bodytype: TypeInfo?,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): Flow<BaseResult<T>> = flow {

        if (callParams.checkNetwork) {
            val isNetworkAvailable = connectivity.isNetworkAvailable()
            if (!isNetworkAvailable) emit(BaseResult.NetworkError)
        }
        val response = ktorClient.delete(route) {
            contentType(ContentType.Application.Json)
            if (body != null && bodytype != null) {
                setBody(body, bodytype)
            }
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value)
                }
                headerParams.forEach {
                    headers.append(it.key, it.value)
                }
                if (callParams.useCache.not()) {
                    headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                }

            }
        }
        val body = response.body<T>(type)
        emit(BaseResult.Success(body))

    }.onStart {
        emit(BaseResult.Loading())
    }.catch {
        emit(handleException(it))
    }

    override suspend fun <T, R> postFlow(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): Flow<BaseResult<T>> = flow {
        if (callParams.checkNetwork) {
            val isNetworkAvailable = connectivity.isNetworkAvailable()
            if (!isNetworkAvailable) emit(BaseResult.NetworkError)
        }
        AppLogger.e("TAG", "REACED ERE")
        val response = ktorClient.post(route) {
            contentType(ContentType.Application.Json)
            if (body != null && bodytype != null) {
                setBody(body, bodytype)
            }
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value)
                }
                headerParams.forEach {
                    headers.append(it.key, it.value)
                }
                if (callParams.useCache.not()) {
                    headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                }

            }
        }
        AppLogger.e("TAG", "REACED ERE2")
        val res = response.body<T>(type)
        emit(BaseResult.Success(res))

    }.onStart {
        AppLogger.e("TAG", "REACED ERE 3")
        emit(BaseResult.Loading())
    }.catch {
        AppLogger.e("TAG", "REACED ERE 3")
        emit(handleException(it))
    }

    override suspend fun <T, R> putFlow(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): Flow<BaseResult<T>> = flow {

        if (callParams.checkNetwork) {
            val isNetworkAvailable = connectivity.isNetworkAvailable()
            if (!isNetworkAvailable) emit(BaseResult.NetworkError)
        }
        val response = ktorClient.put(route) {
            contentType(ContentType.Application.Json)
            if (body != null && bodytype != null) {
                setBody(body, bodytype)
            }
            url {
                queryParams.forEach {
                    parameters.append(it.key, it.value)
                }
                headerParams.forEach {
                    headers.append(it.key, it.value)
                }
                if (callParams.useCache.not()) {
                    headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                }

            }
        }
        val res = response.body<T>(type)
        emit(BaseResult.Success(res))

    }.onStart {
        emit(BaseResult.Loading())
    }.catch {
        emit(handleException(it))
    }

    override suspend fun <T, R> post(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): BaseResult<T> {
        try {
            if (callParams.checkNetwork) {
                val isNetworkAvailable = connectivity.isNetworkAvailable()
                if (!isNetworkAvailable) return BaseResult.NetworkError
            }

            val response = ktorClient.post(route) {
                contentType(ContentType.Application.Json)
                if (body != null && bodytype != null) {
                    setBody(body, bodytype)
                }
                url {
                    queryParams.forEach {
                        parameters.append(it.key, it.value)
                    }
                    headerParams.forEach {
                        headers.append(it.key, it.value)
                    }
                    if (callParams.useCache.not()) {
                        headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                    }

                }
            }
            val body = response.body<T>(type)
            return BaseResult.Success(body)
        } catch (e: Exception) {
            return handleException(e)
        }
    }

    override suspend fun <T, R> put(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): BaseResult<T> {
        try {
            if (callParams.checkNetwork) {
                val isNetworkAvailable = connectivity.isNetworkAvailable()
                if (!isNetworkAvailable) return BaseResult.NetworkError
            }

            val response = ktorClient.put(route) {
                contentType(ContentType.Application.Json)
                if (body != null && bodytype != null) {
                    setBody(body, bodytype)
                }
                url {
                    queryParams.forEach {
                        parameters.append(it.key, it.value)
                    }
                    headerParams.forEach {
                        headers.append(it.key, it.value)
                    }
                    if (callParams.useCache.not()) {
                        headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                    }

                }
            }
            val body = response.body<T>(type)
            return BaseResult.Success(body)
        } catch (e: Exception) {
            return handleException(e)
        }
    }

    

    override suspend fun <T, R> patch(
        route: String,
        body: R?,
        bodytype: TypeInfo?,
        type: TypeInfo,
        queryParams: Map<String, String>,
        headerParams: Map<String, String>,
        callParams: CallParams,
    ): BaseResult<T> {
        try {
            if (callParams.checkNetwork) {
                val isNetworkAvailable = connectivity.isNetworkAvailable()
                if (!isNetworkAvailable) return BaseResult.NetworkError
            }

            val response = ktorClient.patch(route) {
                contentType(ContentType.Application.Json)
                if (body != null && bodytype != null) {
                    setBody(body, bodytype)
                }
                url {
                    queryParams.forEach {
                        parameters.append(it.key, it.value)
                    }
                    headerParams.forEach {
                        headers.append(it.key, it.value)
                    }
                    if (callParams.useCache.not()) {
                        headers.append("Cache-Control", "no-cache, no-store, must-revalidate")
                    }

                }
            }
            val resbody = response.body<T>(type)
            return BaseResult.Success(resbody)
        } catch (e: Exception) {
            return handleException(e)
        }
    }


    private suspend fun <T> handleException(e: Throwable): BaseResult<T> {
        return when (e) {
            is RedirectResponseException -> {
                BaseResult.Error(message = ConstantErrorStrings.UNKNOWN_ERROR)
            }

            is ClientRequestException -> {
               
                    BaseResult.Error(message = ConstantErrorStrings.UNKNOWN_ERROR)
            }

            is HttpRequestTimeoutException -> {
                BaseResult.NetworkError
            }

            is ServerResponseException -> {
             
                    BaseResult.Error(message = ConstantErrorStrings.UNKNOWN_ERROR)
            }

            is SocketTimeoutException -> {
                BaseResult.NetworkError
            }

            is CancellationException -> {
                BaseResult.Error(message = ConstantErrorStrings.CANCELLED_ERROR)
            }

            is UnknownHostException -> {
                BaseResult.NetworkError
            }

            else -> {
                e.printStackTrace()
                BaseResult.Error(message = ConstantErrorStrings.UNKNOWN_ERROR)
            }
        }
    }
}

fun<T,R> Flow<BaseResult<T>>.toEntity(mapToEntity: suspend CoroutineScope.(T)->R):Flow<BaseResult<R>> {
    val response = this.map { result ->
        when (result) {
            is BaseResult.Success -> {
                val mappedResult = CoroutineScope(Dispatchers.IO).async {
                    val res =
                        BaseResult.Success(mapToEntity(result.data))
                    return@async res
                }
                return@map  mappedResult.await()
            }

            is BaseResult.Error -> BaseResult.Error<R>(result.message)

            is BaseResult.Loading -> BaseResult.Loading<R>()
            is BaseResult.NetworkError -> BaseResult.NetworkError
            is BaseResult.NoInternet -> BaseResult.NoInternet
        }
    }
    return response
}