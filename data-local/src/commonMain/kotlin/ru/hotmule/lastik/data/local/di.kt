package ru.hotmule.lastik.data.local

import org.kodein.di.*

val localDataModule = DI.Module("localData") {

    bindSingleton { DriverFactory(di).create() }
    bindSingleton { LastikDatabase(instance()) }

    bindSingleton { instance<LastikDatabase>().albumQueries }
    bindSingleton { instance<LastikDatabase>().artistQueries }
    bindSingleton { instance<LastikDatabase>().friendQueries }
    bindSingleton { instance<LastikDatabase>().profileQueries }
    bindSingleton { instance<LastikDatabase>().scrobbleQueries }
    bindSingleton { instance<LastikDatabase>().topQueries }
    bindSingleton { instance<LastikDatabase>().trackQueries }
}