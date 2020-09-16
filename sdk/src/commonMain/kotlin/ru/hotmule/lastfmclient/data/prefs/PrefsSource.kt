package ru.hotmule.lastfmclient.data.prefs

import com.russhwolf.settings.Settings

class PrefsSource(private val settings: Settings) {

    companion object {
        const val API_KEY_ARG = "apiKey"
        const val TOKEN_ARG = "token"
        const val SECRET_ARG = "secret"
        const val SESSION_KEY_ARG = "sessionKey"
    }

    var apiKey: String?
        get() = getSetting(API_KEY_ARG)
        set(value) { setSetting(value, API_KEY_ARG) }

    var token: String?
        get() = getSetting(TOKEN_ARG)
        set(value) { setSetting(value, TOKEN_ARG) }

    var secret: String?
        get() = getSetting(SECRET_ARG)
        set(value) { setSetting(value, SECRET_ARG) }

    var sessionKey: String?
        get() = getSetting(SESSION_KEY_ARG)
        set(value) { setSetting(value, SESSION_KEY_ARG) }

    private fun getSetting(arg: String) = settings.getStringOrNull(arg)

    private fun setSetting(value: String?, arg: String) {
        if (value != null)
            settings.putString(arg, value)
        else
            settings.remove(arg)
    }
}