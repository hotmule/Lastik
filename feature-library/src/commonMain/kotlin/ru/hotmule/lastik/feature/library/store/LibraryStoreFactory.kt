package ru.hotmule.lastik.feature.library.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class LibraryStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): LibraryStore =
        object : LibraryStore, Store<Intent, State, Nothing> by storeFactory.create(
            LibraryStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CheckDetectedTrack -> {
                    if (intent.isPlaying != null && intent.artist != null && intent.track != null) {
                        dispatch(Result.TrackDetected(intent.isPlaying, intent.artist, intent.track))
                    }
                }
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.TrackDetected -> copy(
                isPlaying = result.isPlaying,
                artist = result.artist,
                track = result.track
            )
        }
    }
}