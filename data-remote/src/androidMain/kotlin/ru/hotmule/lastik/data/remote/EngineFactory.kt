package ru.hotmule.lastik.data.remote

import Lastik.BuildConfig
import android.content.Context
import android.webkit.WebView
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.concurrent.TimeUnit

actual class EngineFactory actual constructor(override val di: DI): DIAware {

    private val context: Context by instance()

    actual fun create(): HttpClientEngine = OkHttp.create {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(5L, TimeUnit.SECONDS)
        }
    }

    actual fun isLoggingEnabled(): Boolean = BuildConfig.DEBUG
    actual fun getUserAgent(): String? = WebView(context).settings.userAgentString
}