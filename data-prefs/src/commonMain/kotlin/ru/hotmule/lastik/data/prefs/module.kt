package ru.hotmule.lastik.data.prefs

import org.kodein.di.*

val prefsDataModule = DI.Module("prefsData") {
    bindSingleton { PackageManager(di) }
    bindSingleton { PrefsStore(instance()) }
}