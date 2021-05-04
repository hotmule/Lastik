package ru.hotmule.lastik.data.prefs

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ObservableSettings
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

actual class SettingsFactory actual constructor(override val di: DI) : DIAware {

    private val context: Context by instance()

    actual fun create(): ObservableSettings = AndroidSettings(
        delegate = context.getSharedPreferences(
            PrefsStore.USER_DATA_PREFS,
            Context.MODE_PRIVATE
        )
    )
}