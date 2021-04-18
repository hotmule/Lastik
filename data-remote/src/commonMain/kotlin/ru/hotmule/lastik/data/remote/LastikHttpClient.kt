package ru.hotmule.lastik.data.remote

import co.touchlab.kermit.Kermit
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.hotmule.lastik.data.remote.api.AuthApi

class LastikHttpClient {

    private val kermit = Kermit()
    private val config = LastikHttpClientConfig()

    private val client = HttpClient(config.engine) {

        if (config.loggingEnabled) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        kermit.d("Ktor") {
                            message
                        }
                    }
                }
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        config.userAgent?.let {
            install(UserAgent) {
                agent = it
            }
        }

        expectSuccess = false

        HttpResponseValidator {
            validateResponse {

                kermit.d("mytag") { it.status.description }

                if (!it.status.isSuccess()) {
                    when (it.status.value) {
                        //HttpStatusCode.Unauthorized -> interactor.signOut()
                        else -> {

                            var errorMessage = "Unknown error"

                            it.content.readUTF8Line()?.let { error ->
                                kotlinx.serialization.json.Json.parseToJsonElement(error)
                                    .jsonObject["message"]
                                    ?.jsonPrimitive
                                    ?.contentOrNull
                                    ?.let { message -> errorMessage = message }
                            }

                            error(errorMessage)
                        }
                    }
                }

                handleResponseException { throwable ->

                    error(throwable.message ?: "Unknown Error")
                }
            }
        }
    }

    val authApi = AuthApi(client, config.apiKey, config.secret)
}