package ru.hotmule.lastik

import android.content.Context
import android.webkit.WebSettings
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.russhwolf.settings.AndroidSettings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.engine.okhttp.OkHttp
import ru.hotmule.lastik.data.remote.HttpClientFactory
import java.util.concurrent.TimeUnit

const val USER_DATA_PREFS = "userDataPreferences"

fun Sdk.Companion.create(
    context: Context,
    isDebug: Boolean,
    apiKey: String,
    secret: String
): Sdk {
    Napier.base(DebugAntilog())
    return Sdk(
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
        AndroidSqliteDriver(
            Database.Schema,
            context,
            "lastik.db"
        ),
        AndroidSettings(
            context.getSharedPreferences(
                USER_DATA_PREFS,
                Context.MODE_PRIVATE
            )
        ),
        apiKey,
        secret
    )
}