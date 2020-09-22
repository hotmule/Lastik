package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ScrobblesInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi
) {
    suspend fun refreshScrobbles() {
        val scrobbles = api.getRecentTracks(prefs.name)
        scrobbles.toString()
    }
}