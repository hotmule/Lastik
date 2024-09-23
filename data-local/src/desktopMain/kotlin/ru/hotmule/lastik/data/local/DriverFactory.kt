package ru.hotmule.lastik.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.kodein.di.DI
import org.kodein.di.DIAware

actual class DriverFactory actual constructor(override val di: DI): DIAware {

    actual fun create(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        LastikDatabase.Schema.create(driver)
        return driver
    }
}