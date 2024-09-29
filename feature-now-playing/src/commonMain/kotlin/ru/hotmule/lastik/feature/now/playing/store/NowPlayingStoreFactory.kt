package ru.hotmule.lastik.feature.now.playing.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.now.playing.NowPlayingComponent
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class NowPlayingStoreFactory(
    private val storeFactory: StoreFactory,
    private val db: LastikDatabase,
    private val prefs: PrefsStore,
    private val api: TrackApi
) {
    companion object {
        private const val MIN_DURATION = 30 * 1000L
        private const val MAX_DELAY = 4 * 60 * 1000L
    }

    fun create(): NowPlayingStore =
        object : NowPlayingStore,
            Store<NowPlayingStore.Intent, NowPlayingStore.State, Nothing> by storeFactory.create(
                NowPlayingStore::class.simpleName,
                initialState = NowPlayingStore.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<NowPlayingStore.Intent, Nothing, NowPlayingStore.State, NowPlayingStore.Result, Nothing>(
            AppCoroutineDispatcher.Main
        ) {
        private var scrobbleJob: Job? = null

        override fun executeIntent(intent: NowPlayingStore.Intent) {
            scope.launch {
                if (intent.packageName in prefs.getScrobbleApps()) {
                    when (intent) {
                        is NowPlayingStore.Intent.CheckDetectedTrack -> dispatch(
                            NowPlayingStore.Result.TrackDetected(
                                intent.track
                            )
                        )

                        is NowPlayingStore.Intent.CheckPlayState -> {

                            dispatch(NowPlayingStore.Result.PlayStateChanged(intent.isPlaying))

                            if (!intent.isPlaying) {

                                scrobbleJob?.cancel()

                            } else {

                                val track = state().track?.name
                                val artist = state().track?.artist
                                val duration = state().track?.duration ?: 0

                                if (track != null &&
                                    artist != null &&
                                    duration > MIN_DURATION
                                ) {
                                    withContext(AppCoroutineDispatcher.IO) {
                                        launch {
                                            /*
                                            api.updateNowPlaying(
                                                track,
                                                artist,
                                                state().track?.album,
                                                state().track?.duration?.div(1000),
                                                state().track?.albumArtist
                                            )
                                            */
                                        }
                                    }

                                    scrobbleJob = withContext(AppCoroutineDispatcher.IO) {
                                        launch {

                                            val delayMillis = with(duration / 2) {
                                                if (this < MAX_DELAY) this else MAX_DELAY
                                            }

                                            delay(delayMillis)
                                            //saveScrobble(state().track)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun saveScrobble(track: NowPlayingComponent.Track?) {
            track?.let {
                val artistId = insertArtist(it.artist)
                val albumId = insertAlbum(artistId, it.album)
                val trackId = insertTrack(artistId, albumId, it.name, it.duration, it.albumArtist)
                insertScrobble(trackId, Clock.System.now().epochSeconds)
            }
        }

        private fun insertArtist(
            name: String?
        ) = if (name != null) {
            with(db.artistQueries) {
                insert(name)
                getId(name).executeAsOneOrNull()
            }
        } else null

        private fun insertAlbum(
            artistId: Long?,
            name: String?
        ) = if (artistId != null && name != null) {
            with(db.albumQueries) {
                insert(artistId, name, null, null)
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun insertTrack(
            artistId: Long?,
            albumId: Long?,
            name: String?,
            duration: Long?,
            albumArtist: String?
        ) = if (artistId != null && name != null) {
            with(db.trackQueries) {
                upsertLocalTrack(albumId, duration, albumArtist, artistId, name)
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun insertScrobble(
            trackId: Long?,
            trackDate: Long?
        ) {
            if (trackId != null && trackDate != null) {
                db.scrobbleQueries.insert(trackId, trackDate, false)
            }
        }
    }

    object ReducerImpl : Reducer<NowPlayingStore.State, NowPlayingStore.Result> {
        override fun NowPlayingStore.State.reduce(msg: NowPlayingStore.Result): NowPlayingStore.State =
            when (msg) {
                is NowPlayingStore.Result.PlayStateChanged -> copy(isPlaying = msg.isPlaying)
                is NowPlayingStore.Result.TrackDetected -> copy(track = msg.track)
                NowPlayingStore.Result.TrackIncorrect -> copy(
                    isPlaying = false,
                    track = null
                )
            }
    }
}
