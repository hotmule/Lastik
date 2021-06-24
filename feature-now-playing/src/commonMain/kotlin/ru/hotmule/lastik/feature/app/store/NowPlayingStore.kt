package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.app.NowPlayingComponent.*
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.utils.Bitmap

internal interface NowPlayingStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class CheckDetectedTrack(val packageName: String, val track: Track) : Intent()
        data class CheckPlayState(val isPlaying: Boolean) : Intent()
    }

    sealed class Result {
        data class TrackChecked(val track: Track) : Result()
        data class PlayStateChanged(val isPlaying: Boolean) : Result()
    }

    data class State(
        val track: Track = Track(),
        val isPlaying: Boolean = false
    )
}