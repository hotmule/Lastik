package ru.hotmule.lastfmclient.data.remote

import com.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HttpClientFactory(
    private val loggingEnabled: Boolean,
    private val userAgent: String,
    private val engine: HttpClientEngine
) {

    fun create() = HttpClient(engine) {

        if (loggingEnabled) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "Ktor", message = message)
                    }
                }
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(UserAgent) {
            agent = userAgent
        }

        HttpResponseValidator {
            validateResponse {

                delay(500)

                if (!it.status.isSuccess()) {
                    error(it.status.value)
                }

                handleResponseException { throwable ->
                    error(
                        throwable.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}