package ru.hotmule.lastik.data.sdk.prefs

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import platform.Foundation.NSUserDefaults

actual class SettingsFactory actual constructor(override val di: DI) : DIAware {

    actual fun getSettings(): ObservableSettings = NSUserDefaultsSettings(
        NSUserDefaults.standardUserDefaults()
    )
}
