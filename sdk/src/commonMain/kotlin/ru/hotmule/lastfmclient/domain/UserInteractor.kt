package ru.hotmule.lastfmclient.domain

import ru.hotmule.lastfmclient.data.prefs.PrefsSource

class UserInteractor(
    private val prefs: PrefsSource,
) {
    fun getName() = prefs.name ?: "Unknown"
}