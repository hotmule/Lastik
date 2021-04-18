package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.JvmPreferencesSettings
import java.util.prefs.Preferences

@Suppress("FunctionName")
fun DesktopPrefs() = JvmPreferencesSettings(
    Preferences
        .userRoot()
        .node(PrefsStore.USER_DATA_PREFS)
)