package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.nullableString
import kotlinx.coroutines.flow.map

class PrefsStore(
    settings: ObservableSettings
) {

    companion object {
        const val USER_DATA_PREFS = "userDataPreferences"
    }

    var token by settings.nullableString()
    var login by settings.nullableString()
    var password by settings.nullableString()

    var sessionKey by settings.nullableString()

    val tokenReceived = settings.getStringOrNullFlow("token").map { it != null }
    val isSessionActive = settings.getStringOrNullFlow("sessionKey").map { it != null }
}