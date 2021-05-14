package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.feature.app.store.ScrobblerStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class ScrobblerStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): ScrobblerStore =
        object : ScrobblerStore, Store<Intent, State, Nothing> by storeFactory.create(
            ScrobblerStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CheckPlayState -> dispatch(Result.PlayStateChanged(intent.isPlaying ?: false))
                is Intent.CheckDetectedTrack -> dispatch(
                    if (intent.artist != null && intent.track != null) {
                        Result.TrackDetected(
                            artist = intent.albumArtist ?: intent.artist,
                            track = intent.track,
                            album = intent.album,
                            art = intent.art
                        )
                    } else {
                        Result.PlayStateChanged(false)
                    }
                )
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.PlayStateChanged -> copy(isPlaying = result.isPlaying)
            is Result.TrackDetected -> copy(
                artist = result.artist,
                track = result.track,
                album = result.album,
                art = result.art
            )
        }
    }
}