package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class NowPlayingStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefs: PrefsStore,
    private val api: TrackApi
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
            if (intent.packageName in prefs.getScrobbleApps()) {
                when (intent) {
                    is Intent.CheckDetectedTrack -> dispatch(Result.TrackDetected(intent.track))
                    is Intent.CheckPlayState -> {

                        val track = getState().track.name
                        val artist = getState().track.artist

                        if (intent.isPlaying && track != null && artist != null) {

                            dispatch(Result.PlayStateChanged(intent.isPlaying))

                            api.updateNowPlaying(
                                track,
                                artist,
                                getState().track.album,
                                getState().track.duration?.div(1000),
                                getState().track.albumArtist
                            )?.nowPlaying?.also {

                                if (it.ignoredMessage?.code != "0") {

                                    dispatch(
                                        Result.PlayStateChanged(
                                            isPlaying = false
                                        )
                                    )

                                } else {

                                    if (it.artist?.corrected != "0" ||
                                        it.album?.corrected != "0" ||
                                        it.track?.corrected != "0" ||
                                        it.albumArtist?.corrected != "0"
                                    ) {
                                        dispatch(
                                            Result.TrackDetected(
                                                getState().track.copy(
                                                    artist = it.artist?.title,
                                                    album = it.album?.title,
                                                    name = it.track?.title,
                                                    albumArtist = it.albumArtist?.title
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.PlayStateChanged -> copy(isPlaying = result.isPlaying)
            is Result.TrackDetected -> copy(track = result.track)
        }
    }
}