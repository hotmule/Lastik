package ru.hotmule.lastik.feature.library.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.library.store.LibraryStore.*

internal interface LibraryStore: Store<Intent, State, Nothing> {

    sealed class Intent {
        data class CheckDetectedTrack(val isPlaying: Boolean?, val artist: String?, val track: String?): Intent()
    }

    sealed class Result {
        data class TrackDetected(val isPlaying: Boolean, val artist: String, val track: String): Result()
    }

    data class State(
        val isPlaying: Boolean = false,
        val artist: String = "",
        val track: String = ""
    )
}