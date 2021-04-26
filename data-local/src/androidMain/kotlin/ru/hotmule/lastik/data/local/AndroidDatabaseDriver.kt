package ru.hotmule.lastik.data.local

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver

@Suppress("FunctionName")
fun AndroidDatabaseDriver(
    context: Context
) = AndroidSqliteDriver(
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