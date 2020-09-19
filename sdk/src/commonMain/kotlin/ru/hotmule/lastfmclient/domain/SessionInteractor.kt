package ru.hotmule.lastfmclient.domain

import ru.hotmule.lastfmclient.data.prefs.PrefsSource

class SessionInteractor(
    private val prefs: PrefsSource
) {

    fun signOut() {
        prefs.clear()
        prefs.isSessionActive.value = false
    }
}