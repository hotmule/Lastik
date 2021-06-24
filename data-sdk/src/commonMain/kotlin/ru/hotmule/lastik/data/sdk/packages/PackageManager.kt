package ru.hotmule.lastik.data.sdk.packages

import org.kodein.di.DI
import org.kodein.di.DIAware

expect class PackageManager(di: DI): DIAware {
    suspend fun getApps(): List<Package>
}