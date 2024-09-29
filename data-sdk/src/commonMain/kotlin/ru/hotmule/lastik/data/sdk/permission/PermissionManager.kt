package ru.hotmule.lastik.data.sdk.permission

import org.kodein.di.DI
import org.kodein.di.DIAware

expect class PermissionManager(di: DI): DIAware {

    fun isNotificationsAccessGranted(): Boolean

    fun requestNotificationsAccess()
}
