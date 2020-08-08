package ru.hotmule.lastfmclient

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import io.ktor.client.engine.okhttp.OkHttp
import ru.hotmule.lastfmclient.data.remote.HttpClientFactory

const val USER_DATA_PREFS = "userDataPreferences"

fun Sdk.Companion.create(
    context: Context,
    isDebug: Boolean,
    endpoint: String
) = Sdk(
    isDebug,
    endpoint,
    HttpClientFactory(
        OkHttp.create { }
    ),
    AndroidSettings(
        context.getSharedPreferences(
            USER_DATA_PREFS,
            Context.MODE_PRIVATE
        )
    )
)