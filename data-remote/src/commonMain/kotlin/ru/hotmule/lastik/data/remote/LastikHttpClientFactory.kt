package ru.hotmule.lastik.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore

class LastikHttpClientFactory(
    private val engineFactory: EngineFactory,
    private val prefsStore: PrefsStore
) {
    fun create() = HttpClient(engineFactory.create()) {

        expectSuccess = false

        if (engineFactory.isLoggingEnabled()) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger
                            .withTag("Ktor")
                            .d { message }
                    }
                }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
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

                            Json.parseToJsonElement(it.bodyAsText())
                                .jsonObject["message"]
                                ?.jsonPrimitive
                                ?.contentOrNull
                                ?.let { message -> errorMessage = message }

                            error(errorMessage)
                        }
                    }
                }
            }

            handleResponseExceptionWithRequest { cause, _ ->
                error(cause.message ?: "Unknown Error")
            }
        }
    }
}