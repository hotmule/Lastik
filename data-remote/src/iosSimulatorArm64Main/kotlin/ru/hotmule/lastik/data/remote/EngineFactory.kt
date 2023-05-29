package ru.hotmule.lastik.data.remote

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.kodein.di.DI
import org.kodein.di.DIAware
import platform.Foundation.valueForKey
import platform.WebKit.WKWebView

actual class EngineFactory actual constructor(override val di: DI) : DIAware {

    actual fun create(): HttpClientEngine = Darwin.create {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }

    actual fun isLoggingEnabled(): Boolean = false

    actual fun getUserAgent(): String? = WKWebView().valueForKey("userAgent")?.toString()
}
