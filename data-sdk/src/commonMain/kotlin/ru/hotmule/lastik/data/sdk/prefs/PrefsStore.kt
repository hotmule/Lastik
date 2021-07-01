package ru.hotmule.lastik.data.sdk.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.int
import com.russhwolf.settings.nullableString
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PrefsStore(
    private val factory: SettingsFactory,
    private val settings: ObservableSettings = factory.getSettings()
) {

    companion object {
        const val USER_DATA_PREFS = "userDataPreferences"
    }

    var token by settings.nullableString()
    var login by settings.nullableString()
    var password by settings.nullableString()
    var sessionKey by settings.nullableString()
    private var scrobbleApps by settings.nullableString()

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

    fun getScrobbleApps() = Json.decodeFromString<List<String>>(
        settings.getString("scrobbleApps", "[]")
    )

    fun saveScrobbleApp(
        packageName: String,
        enabledNow: Boolean
    ) {
        scrobbleApps = Json.encodeToString(
            Json
                .decodeFromString<Set<String>>(scrobbleApps ?: "[]")
                .toMutableSet()
                .apply { if (enabledNow) remove(packageName) else add(packageName) }
        )
    }

    fun clear() {
        settings.clear()
    }
}