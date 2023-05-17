package ru.hotmule.lastik.data.sdk.prefs

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

actual class SettingsFactory actual constructor(override val di: DI) : DIAware {

    private val context: Context by instance()

    actual fun getSettings(): ObservableSettings = SharedPreferencesSettings(
        delegate = context.getSharedPreferences(
            PrefsStore.USER_DATA_PREFS,
            Context.MODE_PRIVATE
        )
    )
}