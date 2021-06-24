package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class NowPlayingStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore
) {
    fun create(): NowPlayingStore =
        object : NowPlayingStore, Store<Intent, State, Nothing> by storeFactory.create(
            NowPlayingStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CheckPlayState -> dispatch(Result.PlayStateChanged(intent.isPlaying))
                is Intent.CheckDetectedTrack -> {
                    if (intent.packageName in prefsStore.getScrobbleApps()) {
                        dispatch(Result.TrackChecked(intent.track))
                    }
                }
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.PlayStateChanged -> copy(isPlaying = result.isPlaying)
            is Result.TrackChecked -> copy(track = result.track,)
        }
    }
}