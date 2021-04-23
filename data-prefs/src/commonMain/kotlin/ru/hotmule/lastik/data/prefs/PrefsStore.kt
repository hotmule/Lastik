package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
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

    val tokenReceived = settings.getStringOrNullFlow("token").map { it != null }
    val isSessionActive = settings.getStringOrNullFlow("sessionKey").map { it != null }

    private var artistsPeriod by settings.int(defaultValue = 0)
    private var albumsPeriod by settings.int(defaultValue = 0)
    private var tracksPeriod by settings.int(defaultValue = 0)

    fun saveShelfPeriod(shelfIndex: Int, periodIndex: Int) {
        when (shelfIndex) {
            1 -> artistsPeriod = periodIndex
            2 -> albumsPeriod = periodIndex
            3 -> tracksPeriod = periodIndex
        }
    }

    fun getShelfPeriod(shelfIndex: Int) = when (shelfIndex) {
        1 -> artistsPeriod
        2 -> albumsPeriod
        else -> tracksPeriod
    }

    fun clear() {
        settings.clear()
    }
}