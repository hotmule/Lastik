package ru.hotmule.lastik.feature.now.playing.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.now.playing.NowPlayingComponent

internal interface NowPlayingStore : Store<NowPlayingStore.Intent, NowPlayingStore.State, Nothing> {

    sealed class Intent(val packageName: String) {
        data class CheckDetectedTrack(
            val packName: String,
            val track: NowPlayingComponent.Track
        ) : Intent(packName)
        data class CheckPlayState(
            val packName: String,
            val isPlaying: Boolean
        ) : Intent(packName)
    }

    sealed class Result {
        data class PlayStateChanged(val isPlaying: Boolean) : Result()
        data class TrackDetected(val track: NowPlayingComponent.Track?) : Result()
        data object TrackIncorrect : Result()
    }

    data class State(
        val isPlaying: Boolean = false,
        val track: NowPlayingComponent.Track? = null
    )
}
