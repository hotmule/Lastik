package ru.hotmule.lastik.data.local

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

actual class DriverFactory actual constructor(override val di: DI): DIAware {

    private val context: Context by di.instance()

    actual fun create(): SqlDriver = AndroidSqliteDriver(
        schema = LastikDatabase.Schema,
        context = context,
        name = "lastik.db",
        callback = object : AndroidSqliteDriver.Callback(LastikDatabase.Schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                super.onConfigure(db)
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}