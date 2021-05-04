package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val prefsDataModule = DI.Module("prefsData") {
    bind<ObservableSettings>() with singleton { SettingsFactory(di).create() }
    bind<PrefsStore>() with singleton { PrefsStore(instance()) }
}