package ru.hotmule.lastik.data.remote

import co.touchlab.kermit.Kermit
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore

class LastikHttpClientFactory(
    private val engineFactory: EngineFactory,
    private val prefsStore: PrefsStore,
    private val kermit: Kermit
) {
    fun create() = HttpClient(engineFactory.create()) {

        expectSuccess = false

        if (engineFactory.isLoggingEnabled()) {
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

        val userAgent = engineFactory.getUserAgent()
        if (userAgent != null)
            install(UserAgent) { agent = userAgent }
        else
            BrowserUserAgent()

        HttpResponseValidator {
            validateResponse {

                if (!it.status.isSuccess()) {
                    when (it.status) {
                        HttpStatusCode.Unauthorized -> prefsStore.clear()
                        else -> {

                            var errorMessage = "Unknown error"

                            it.content.readUTF8Line()?.let { error ->
                                Json.parseToJsonElement(error)
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
}