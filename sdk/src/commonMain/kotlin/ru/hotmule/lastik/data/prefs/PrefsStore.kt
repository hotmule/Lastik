package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.Settings

class PrefsStore(private val settings: Settings) {

    companion object {
        const val TOKEN_ARG = "token"
        const val SESSION_KEY_ARG = "sessionKey"
    }

    var token: String?
        get() = getSetting(TOKEN_ARG)
        set(value) { setSetting(value, TOKEN_ARG) }

    var sessionKey: String?
        get() = getSetting(SESSION_KEY_ARG)
        set(value) { setSetting(value, SESSION_KEY_ARG) }

    fun clear() {
        settings.clear()
    }

    private fun getSetting(arg: String) = settings.getStringOrNull(arg)

    private fun setSetting(value: String?, arg: String) {
        if (value != null)
            settings.putString(arg, value)
        else
            settings.remove(arg)
    }
}