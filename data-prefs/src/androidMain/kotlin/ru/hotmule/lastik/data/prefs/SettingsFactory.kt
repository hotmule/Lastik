package ru.hotmule.lastik.data.prefs

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ObservableSettings

actual class SettingsFactory(private val context: Context) {
    actual fun create(): ObservableSettings = AndroidSettings(
        delegate = context.getSharedPreferences(
            PrefsStore.USER_DATA_PREFS,
            Context.MODE_PRIVATE
        )
    )
}