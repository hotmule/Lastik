package ru.hotmule.lastfmclient.domain

import ru.hotmule.lastfmclient.data.settings.PrefsStore

class ProfileInteractor(
    private val prefs: PrefsStore,
) {
    fun getName() = prefs.name ?: "Unknown"
}