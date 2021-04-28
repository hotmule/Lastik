package ru.hotmule.lastik.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.http.*
import kotlinx.serialization.json.Json

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

                /*
                if (!it.status.isSuccess()) {
                    when (it.status.value) {
                        401 -> interactor.signOut()
                        else -> error(it.status.value)
                    }
                }
                 */

                handleResponseException { throwable ->
                    error(throwable.message ?: "Unknown Error")
                }
            }
        }
    }
}