package ru.hotmule.lastfmclient

import android.content.Context
import android.webkit.WebSettings
import com.russhwolf.settings.AndroidSettings
import io.ktor.client.engine.okhttp.OkHttp
import ru.hotmule.lastfmclient.data.remote.HttpClientFactory
import java.util.concurrent.TimeUnit

const val USER_DATA_PREFS = "userDataPreferences"

fun Sdk.Companion.create(
    context: Context,
    isDebug: Boolean
) = Sdk(
    HttpClientFactory(
        loggingEnabled = isDebug,
        userAgent = WebSettings.getDefaultUserAgent(context),
        engine = OkHttp.create {
            config {
                retryOnConnectionFailure(true)
                connectTimeout(5L, TimeUnit.SECONDS)
            }
        }
    ),
    AndroidSettings(
        context.getSharedPreferences(
            USER_DATA_PREFS,
            Context.MODE_PRIVATE
        )
    )
)