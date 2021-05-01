package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

@Suppress("FunctionName")
fun DesktopDatabaseDriver() = JdbcSqliteDriver(
    url = JdbcSqliteDriver.IN_MEMORY + File(
        System.getProperty("java.io.tmpdir"),
        "lastik.db"
    ).absolutePath
)