package ru.hotmule.lastik.data.remote

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import java.util.concurrent.TimeUnit

actual class EngineFactory actual constructor(override val di: DI): DIAware {

    actual fun create(): HttpClientEngine = OkHttp.create {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5L, TimeUnit.SECONDS)
        }
    }

    actual fun isLoggingEnabled(): Boolean = false
    actual fun getUserAgent(): String? = null
}