package ru.hotmule.lastfmclient.data.remote

import com.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.AuthProvider
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.auth.HttpAuthHeader
import kotlinx.serialization.json.Json
import ru.hotmule.lastfmclient.data.prefs.PrefsSource

class HttpClientFactory(private val engine: HttpClientEngine) {

    fun create(loggingEnabled: Boolean, prefsSource: PrefsSource) = HttpClient(engine) {

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

        install(Auth) {
            providers.add(
                object : AuthProvider {
                    override val sendWithoutRequest = true
                    override fun isApplicable(auth: HttpAuthHeader) = true
                    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
                        prefsSource.token?.let { token ->
                            request.headers["Session"] = token
                        }
                    }
                }
            )
        }
    }
}