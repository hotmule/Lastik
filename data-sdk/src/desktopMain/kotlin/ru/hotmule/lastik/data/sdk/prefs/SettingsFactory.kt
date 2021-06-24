package ru.hotmule.lastik.data.sdk.prefs

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import java.util.prefs.Preferences

actual class SettingsFactory actual constructor(override val di: DI) : DIAware {

    actual fun getSettings(): ObservableSettings = JvmPreferencesSettings(
        Preferences.userRoot()
            .node(PrefsStore.USER_DATA_PREFS)
    )
}