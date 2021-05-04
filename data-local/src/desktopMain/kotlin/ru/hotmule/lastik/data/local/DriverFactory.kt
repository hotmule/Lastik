package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

actual class DriverFactory {
    actual fun create(): SqlDriver = with(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)) {
        LastikDatabase.Schema.create(this)
        this
    }
}