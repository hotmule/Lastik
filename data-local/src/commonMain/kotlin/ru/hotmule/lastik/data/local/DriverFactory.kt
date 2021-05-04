package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.DIAware

expect class DriverFactory(di: DI): DIAware {
    fun create(): SqlDriver
}