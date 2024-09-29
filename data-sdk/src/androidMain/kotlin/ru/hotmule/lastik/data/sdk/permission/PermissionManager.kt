package ru.hotmule.lastik.data.sdk.permission

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

actual class PermissionManager actual constructor(override val di: DI) : DIAware {

    private val context: Context by instance()

    actual fun isNotificationsAccessGranted(): Boolean {
        return NotificationManagerCompat
            .getEnabledListenerPackages(context)
            .contains(context.packageName)
    }

    actual fun requestNotificationsAccess() {
        context.startActivity(
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}
