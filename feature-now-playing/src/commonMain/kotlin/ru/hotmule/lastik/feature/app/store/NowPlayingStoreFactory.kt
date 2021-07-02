package ru.hotmule.lastik.feature.app.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.remote.entities.Image
import ru.hotmule.lastik.data.remote.entities.LibraryItem
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
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
        object : NowPlayingStore, Store<Intent, State, Nothing> by storeFactory.create(
            NowPlayingStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        private var scrobbleJob: Job? = null

        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            if (intent.packageName in prefs.getScrobbleApps()) {
                when (intent) {
                    is Intent.CheckDetectedTrack -> dispatch(Result.TrackDetected(intent.track))
                    is Intent.CheckPlayState -> {

                        dispatch(Result.PlayStateChanged(intent.isPlaying))

                        if (!intent.isPlaying) {

                            scrobbleJob?.cancel()

                        } else {

                            val track = getState().track?.name
                            val artist = getState().track?.artist
                            val duration = getState().track?.duration ?: 0

                            if (track != null &&
                                artist != null &&
                                duration > MIN_DURATION
                            ) {
                                withContext(AppCoroutineDispatcher.IO) {
                                    launch {
                                        api.updateNowPlaying(
                                            track,
                                            artist,
                                            getState().track?.album,
                                            getState().track?.duration?.div(1000),
                                            getState().track?.albumArtist
                                        )
                                    }
                                }

                                scrobbleJob = withContext(AppCoroutineDispatcher.IO) {
                                    launch {

                                        val delayMillis = with (duration / 2) {
                                            if (this < MAX_DELAY) this else MAX_DELAY
                                        }

                                        delay(delayMillis)
                                        saveScrobble(getState().track)
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

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.PlayStateChanged -> copy(isPlaying = result.isPlaying)
            is Result.TrackDetected -> copy(track = result.track)
            Result.TrackIncorrect -> copy(
                isPlaying = false,
                track = null
            )
        }
    }
}