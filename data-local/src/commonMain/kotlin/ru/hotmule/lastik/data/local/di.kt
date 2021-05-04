package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val localDataModule = DI.Module("localData") {
    bind<SqlDriver>() with singleton { DriverFactory(di).create() }
    bind<LastikDatabase>() with singleton { LastikDatabase(instance()) }
}