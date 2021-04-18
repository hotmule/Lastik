package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.nullableString

class PrefsStore(
    private val settings: ObservableSettings
) {

    companion object {
        const val USER_DATA_PREFS = "userDataPreferences"
    }

    var token by settings.nullableString()
    var sessionKey by settings.nullableString()
}