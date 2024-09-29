package ru.hotmule.lastik.data.sdk.permission

import org.kodein.di.DI
import org.kodein.di.DIAware

actual class PermissionManager actual constructor(override val di: DI) : DIAware {

    actual fun isNotificationsAccessGranted(): Boolean = false

    actual fun requestNotificationsAccess() {
        println("Notifications access requested")
    }
}
