package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings

expect class SettingsFactory {
    fun create(): ObservableSettings
}