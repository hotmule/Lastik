package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.ObservableSettings
import java.util.prefs.Preferences

actual class SettingsFactory {
    actual fun create(): ObservableSettings = JvmPreferencesSettings(
        Preferences
            .userRoot()
            .node(PrefsStore.USER_DATA_PREFS)
    )
}