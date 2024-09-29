package ru.hotmule.lastik.data.sdk

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.hotmule.lastik.data.sdk.permission.PermissionManager
import ru.hotmule.lastik.data.sdk.packages.PackageManager
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.data.sdk.prefs.SettingsFactory

val sdkDataModule = DI.Module("sdkData") {
    bindSingleton { PermissionManager(di) }
    bindSingleton { PackageManager(di) }
    bindSingleton { SettingsFactory(di) }
    bindSingleton { PrefsStore(instance()) }
}
