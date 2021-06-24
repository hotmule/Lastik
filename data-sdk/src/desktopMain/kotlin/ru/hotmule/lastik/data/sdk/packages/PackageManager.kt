package ru.hotmule.lastik.data.sdk.packages

import org.kodein.di.DI
import org.kodein.di.DIAware
import ru.hotmule.lastik.data.sdk.packages.Package

actual class PackageManager actual constructor(override val di: DI) : DIAware {

    actual suspend fun getApps(): List<Package> = emptyList()
}