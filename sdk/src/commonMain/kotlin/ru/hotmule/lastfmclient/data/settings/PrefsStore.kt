package ru.hotmule.lastfmclient.data.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow

class PrefsStore(private val settings: Settings) {

    companion object {
        const val TOKEN_ARG = "token"
        const val NAME_ARG = "name"
        const val SESSION_KEY_ARG = "sessionKey"
    }

    var token: String?
        get() = getSetting(TOKEN_ARG)
        set(value) { setSetting(value, TOKEN_ARG) }

    var name: String?
        get() = getSetting(NAME_ARG)
        set(value) { setSetting(value, NAME_ARG) }

    var sessionKey: String?
        get() = getSetting(SESSION_KEY_ARG)
        set(value) { setSetting(value, SESSION_KEY_ARG) }

    fun clear() {
        settings.clear()
        isSessionActive.value = false
    }

    val isSessionActive = MutableStateFlow(sessionKey != null)

    private fun getSetting(arg: String) = settings.getStringOrNull(arg)

    private fun setSetting(value: String?, arg: String) {
        if (value != null)
            settings.putString(arg, value)
        else
            settings.remove(arg)
    }
}