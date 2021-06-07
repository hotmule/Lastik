package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware

expect class PackageManager(di: DI): DIAware {
    fun getSettings(): ObservableSettings
    fun getApps(): List<String>
}