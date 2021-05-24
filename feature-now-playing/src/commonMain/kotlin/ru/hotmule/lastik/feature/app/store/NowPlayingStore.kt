package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.utils.Bitmap

internal interface NowPlayingStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class CheckPlayState(val isPlaying: Boolean?) : Intent()
        data class CheckDetectedTrack(
            val artist: String?,
            val album: String?,
            val track: String?,
            val art: Bitmap?,
            val duration: Long?,
            val albumArtist: String?
        ) : Intent()
    }

    sealed class Result {
        data class PlayStateChanged(val isPlaying: Boolean) : Result()
        data class TrackDetected(
            val artist: String,
            val track: String,
            val album: String?,
            val art: Bitmap?
        ) : Result()
    }

    data class State(
        val isPlaying: Boolean = false,
        val artist: String = "",
        val album: String? = null,
        val track: String = "",
        val art: Bitmap? = null
    )
}