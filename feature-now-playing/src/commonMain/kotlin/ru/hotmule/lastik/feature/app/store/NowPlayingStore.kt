package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.app.NowPlayingComponent.*
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.utils.Bitmap

internal interface NowPlayingStore : Store<Intent, State, Nothing> {

    sealed class Intent(val packageName: String) {
        data class CheckDetectedTrack(val packName: String, val track: Track) : Intent(packName)
        data class CheckPlayState(val packName: String, val isPlaying: Boolean) : Intent(packName)
    }

    sealed class Result {
        data class PlayStateChanged(val isPlaying: Boolean) : Result()
        data class TrackDetected(val track: Track) : Result()
    }

    data class State(
        val isPlaying: Boolean = false,
        val track: Track = Track()
    )
}