package ru.hotmule.lastik.data.prefs

import android.content.Context
import android.content.Intent
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

actual class PackageManager actual constructor(override val di: DI) : DIAware {

    private val context: Context by instance()

    actual fun getSettings(): ObservableSettings = AndroidSettings(
        delegate = context.getSharedPreferences(
            PrefsStore.USER_DATA_PREFS,
            Context.MODE_PRIVATE
        )
    )

    actual fun getApps() = context.packageManager.queryIntentActivities(
        Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_APP_MUSIC), 0
    ).map {
        it.resolvePackageName
    }
}