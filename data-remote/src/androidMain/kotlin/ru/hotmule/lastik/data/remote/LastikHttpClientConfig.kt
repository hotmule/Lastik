package ru.hotmule.lastik.data.remote

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import java.util.concurrent.TimeUnit

actual class LastikHttpClientConfig {
    actual val apiKey: String = BuildConfig.API_KEY
    actual val secret: String = BuildConfig.SECRET
    actual val userAgent: String? = null
    actual val loggingEnabled: Boolean = BuildConfig.DEBUG
    actual val engine: HttpClientEngine = OkHttp.create {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5L, TimeUnit.SECONDS)
        }
    }
}