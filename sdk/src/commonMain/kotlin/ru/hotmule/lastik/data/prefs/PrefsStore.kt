package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString
import com.russhwolf.settings.string
import kotlinx.coroutines.flow.MutableStateFlow
import ru.hotmule.lastik.data.remote.entities.Period

class PrefsStore(private val settings: Settings) {

    companion object {
        const val TOKEN_ARG = "token"
        const val SESSION_KEY_ARG = "sessionKey"
        const val NAME_ARG = "name"
    }

    var token by settings.nullableString(TOKEN_ARG)
    var name by settings.nullableString(NAME_ARG)

    val isSessionActive = MutableStateFlow(sessionKey != null)
    var sessionKey: String?
        get() = settings.getStringOrNull(SESSION_KEY_ARG)
        set(value) {
            isSessionActive.value = value != null
            settings.apply {
                if (value != null)
                    putString(SESSION_KEY_ARG, value)
                else
                    remove(SESSION_KEY_ARG)
            }
        }

    fun clear() {
        sessionKey = null
        settings.clear()
    }
}