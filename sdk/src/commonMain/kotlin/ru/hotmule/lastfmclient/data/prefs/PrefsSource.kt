package ru.hotmule.lastfmclient.data.prefs

import com.russhwolf.settings.Settings

class PrefsSource(private val settings: Settings) {

    companion object {
        const val TOKEN_ARG = "token"
    }

    var token: String?
        get() = settings.getStringOrNull(TOKEN_ARG)
        set(value) {
            if (value != null)
                settings.putString(TOKEN_ARG, value)
            else
                settings.remove(TOKEN_ARG)
        }
}