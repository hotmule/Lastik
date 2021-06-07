package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import java.util.prefs.Preferences

actual class PackageManager actual constructor(override val di: DI) : DIAware {

    actual fun getSettings(): ObservableSettings = JvmPreferencesSettings(
        Preferences
            .userRoot()
            .node(PrefsStore.USER_DATA_PREFS)
    )

    actual fun getApps(): List<String> = emptyList()
}