package ru.hotmule.lastik.data.sdk

import org.kodein.di.*
import ru.hotmule.lastik.data.sdk.packages.PackageManager
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.data.sdk.prefs.SettingsFactory

val sdkDataModule = DI.Module("sdkData") {
    bindSingleton { PackageManager(di) }
    bindSingleton { SettingsFactory(di) }
    bindSingleton { PrefsStore(instance()) }
}