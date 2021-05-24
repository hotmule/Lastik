package ru.hotmule.lastik

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import org.kodein.di.*
import ru.hotmule.lastik.feature.root.rootComponentModule

class App : Application(), DIAware {

    override val di by DI.lazy {

        import(rootComponentModule)

        bindInstance<Context> { this@App }
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                NotificationChannel(
                    getString(R.string.scrobbler_notification_channel_id),
                    getString(R.string.scrobbler_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        startService(
            Intent(this, PlayerCatcherService::class.java)
        )
    }
}