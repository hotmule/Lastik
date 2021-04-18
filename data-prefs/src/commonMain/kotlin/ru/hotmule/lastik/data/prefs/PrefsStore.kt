package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.nullableString
import kotlinx.coroutines.flow.map

class PrefsStore(
    settings: ObservableSettings
) {

    companion object {
        const val USER_DATA_PREFS = "userDataPreferences"
        const val TOKEN_ARG = "token"
        const val SESSION_KEY_ARG = "sessionKeyArg"
    }

    var token by settings.nullableString(TOKEN_ARG)
    var sessionKey by settings.nullableString(SESSION_KEY_ARG)

    val isSessionActive = settings.getStringOrNullFlow(SESSION_KEY_ARG).map { it != null }
}