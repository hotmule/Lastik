package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware

expect class SettingsFactory(di: DI): DIAware {
    fun create(): ObservableSettings
}