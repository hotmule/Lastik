package ru.hotmule.lastik

import com.russhwolf.settings.JvmPreferencesSettings
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.engine.okhttp.*
import ru.hotmule.lastik.data.remote.HttpClientFactory
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.prefs.Preferences

fun Sdk.Companion.create(
    apiKey: String,
    secret: String
) = Sdk(
    HttpClientFactory(
        loggingEnabled = false,
        userAgent = "",
        engine = OkHttp.create {
            config {
                retryOnConnectionFailure(true)
                connectTimeout(5L, TimeUnit.SECONDS)
            }
        }
    ),
    JdbcSqliteDriver(
        url = JdbcSqliteDriver.IN_MEMORY + File(
            System.getProperty("java.io.tmpdir"),
            "lastik.db"
        ).absolutePath
    ),
    JvmPreferencesSettings(
        Preferences
            .userRoot()
            .node("userDataPreferences")
    ),
    apiKey,
    secret
)