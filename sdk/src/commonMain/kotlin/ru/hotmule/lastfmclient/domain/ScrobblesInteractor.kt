package ru.hotmule.lastfmclient.domain

import ru.hotmule.lastfmclient.data.settings.PrefsStore
import ru.hotmule.lastfmclient.data.remote.api.UserApi

class ScrobblesInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi
) {
    suspend fun refreshScrobbles() {
        val scrobbles = api.getRecentTracks(prefs.name)
        scrobbles.toString()
    }
}