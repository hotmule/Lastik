package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.nullableString

class PrefsStore(settings: ObservableSettings) {

    var name by settings.nullableString()
    var token by settings.nullableString()
    var sessionKey by settings.nullableString()
}