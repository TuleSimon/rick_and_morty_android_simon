package com.simon.data.ktor

import com.simon.data.logger.AppLogger
import com.simon.data.network.apiRoutes.BaseUrls
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Protocol

private const val TIME_OUT: Long = 600_000

internal fun ktorHttpClient() =
    HttpClient(OkHttp) {

        expectSuccess = true
        engine {
            config {
                followRedirects(true)
                protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            }
        }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        AppLogger.v("Logger Ktor => ", message)
                    }
                }
                level = LogLevel.ALL
            }


        install(ContentNegotiation) {

            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    explicitNulls = false
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                },
                contentType = ContentType.Any,
            )
        }

        install(HttpCache)

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BaseUrls.BASE_URL
            }

            contentType(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            val converter = KotlinxSerializationConverter(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    encodeDefaults = true
                },
            )
            register(ContentType.Application.Json, converter)
        }

        install(HttpTimeout) { // 4
            requestTimeoutMillis = TIME_OUT
            connectTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
        }



        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
