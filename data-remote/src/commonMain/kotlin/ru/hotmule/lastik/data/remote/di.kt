package ru.hotmule.lastik.data.remote

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.hotmule.lastik.data.prefs.prefsDataModule

val remoteDataModule = DI.Module("remoteData") {
    import(prefsDataModule)
    bindSingleton { LastikHttpClient(instance()) }
}