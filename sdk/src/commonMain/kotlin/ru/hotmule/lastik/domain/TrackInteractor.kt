package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.TrackQueries
import ru.hotmule.lastik.data.remote.api.TrackApi

class TrackInteractor(
    private val api: TrackApi,
    private val queries: TrackQueries
) {

    suspend fun setLoved(
        track: String,
        artist: String,
        loved: Boolean
    ) {
        api.setLoved(track, artist, loved)
        queries.updateTrackLove(loved, track, artist)
    }
}