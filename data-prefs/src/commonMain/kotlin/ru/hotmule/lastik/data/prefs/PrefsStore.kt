package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.int
import com.russhwolf.settings.nullableString
import kotlinx.coroutines.flow.map

class PrefsStore(
    private val settings: ObservableSettings
) {

    companion object {
        const val USER_DATA_PREFS = "userDataPreferences"
    }

    var token by settings.nullableString()
    var login by settings.nullableString()
    var password by settings.nullableString()
    var sessionKey by settings.nullableString()

    var artistsPeriod by settings.int(defaultValue = 0)
    var albumsPeriod by settings.int(defaultValue = 0)
    var tracksPeriod by settings.int(defaultValue = 0)

    val tokenAsFlow = settings.getStringOrNullFlow("token")
    val isSessionActive = settings.getStringOrNullFlow("sessionKey").map { it != null }

    fun getTopPeriodAsFlow(shelfIndex: Int) = settings.getIntFlow(
        when (shelfIndex) {
            1 -> "artistsPeriod"
            2 -> "albumsPeriod"
            else -> "tracksPeriod"
        }
    )

    fun clear() {
        settings.clear()
    }
}