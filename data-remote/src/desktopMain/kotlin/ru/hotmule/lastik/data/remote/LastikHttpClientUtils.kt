package ru.hotmule.lastik.data.remote

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import java.util.concurrent.TimeUnit

actual class LastikHttpClientConfig {
    actual val apiKey: String = ""
    actual val secret: String = ""
    actual val loggingEnabled: Boolean = false
    actual val engine: HttpClientEngine = OkHttp.create {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5L, TimeUnit.SECONDS)
        }
    }
}