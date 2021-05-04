package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.kodein.di.DI
import org.kodein.di.DIAware
import java.io.File

actual class DriverFactory actual constructor(override val di: DI): DIAware {

    actual fun create(): SqlDriver = with(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)) {
        LastikDatabase.Schema.create(this)
        this
    }
}