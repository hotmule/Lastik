package ru.hotmule.lastik

import android.content.Context
import android.webkit.WebSettings
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.russhwolf.settings.AndroidSettings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.engine.okhttp.OkHttp
import ru.hotmule.lastik.data.local.LastikDatabase
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
    context.deleteDatabase("lastik.db")
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
            schema = LastikDatabase.Schema,
            context = context,
            name = "lastik.db",
            callback = object : AndroidSqliteDriver.Callback(LastikDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        ),
        AndroidSettings(
            delegate = context.getSharedPreferences(
                USER_DATA_PREFS,
                Context.MODE_PRIVATE
            )
        ),
        apiKey,
        secret
    )
}