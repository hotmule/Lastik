package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.kodein.di.DI
import org.kodein.di.DIAware

actual class DriverFactory actual constructor(override val di: DI) : DIAware {

    actual fun create(): SqlDriver = NativeSqliteDriver(
        schema = LastikDatabase.Schema,
        name = "lastik.db",
    )
}
