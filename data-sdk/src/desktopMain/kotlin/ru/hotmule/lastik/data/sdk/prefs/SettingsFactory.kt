package ru.hotmule.lastik.data.sdk.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import java.util.prefs.Preferences

actual class SettingsFactory actual constructor(override val di: DI) : DIAware {

    actual fun getSettings(): ObservableSettings = PreferencesSettings(
        Preferences.userRoot()
            .node(PrefsStore.USER_DATA_PREFS)
    )
}