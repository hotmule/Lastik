package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.Date
import ru.hotmule.lastik.data.remote.entities.Image
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class ShelfStoreFactory(
    private val storeFactory: StoreFactory,
    private val database: LastikDatabase,
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val index: Int
) {

    fun create(): ShelfStore =
        object : ShelfStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "${ShelfStore::class.simpleName} $index",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {

        private val periodNames = arrayOf(
            "overall", "7day", "1month", "3month", "6month", "12month"
        )

        override suspend fun executeAction(
            action: Unit,
            getState: () -> State
        ) {
            val periodName = periodNames[prefs.getShelfPeriod(index)]

            when (index) {
                0 -> updateRecentTracks()
            }
        }

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {

        }

        private suspend fun updateRecentTracks() {
            withContext(AppCoroutineDispatcher.IO) {

                api.getRecentTracks(1)?.recent?.tracks?.forEach { track ->

                    if (track.date?.uts == null && track.attributes?.nowPlaying == "true")
                        track.date = Date(0)

                    database.transaction {

                        val artistId = insertArtist(
                            track.artist?.name
                        )

                        val albumId = insertAlbum(
                            artistId,
                            track.album?.name,
                            track.images
                        )

                        val trackId = insertScrobbleTrack(
                            artistId,
                            albumId,
                            track.name,
                            track.loved,
                        )

                        insertScrobble(
                            trackId,
                            track.date?.uts,
                            track.attributes?.nowPlaying
                        )
                    }
                }
            }
        }

        private fun insertArtist(
            name: String?
        ) = if (name != null) {
            with(database.artistQueries) {
                insert(name)
                getId(name).executeAsOneOrNull()
            }
        } else null

        private fun insertAlbum(
            artistId: Long?,
            name: String?,
            images: List<Image>?,
        ) = if (artistId != null && name != null) {
            with(database.albumQueries) {
                insert(
                    artistId = artistId,
                    name = name,
                    lowArtwork = images?.get(2)?.url,
                    highArtwork = images?.get(3)?.url
                )
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun insertScrobbleTrack(
            artistId: Long?,
            albumId: Long?,
            name: String?,
            loved: Int?
        ) = if (artistId != null && name != null) {
            with(database.trackQueries) {
                upsertRecentTrack(
                    artistId = artistId,
                    albumId = albumId,
                    name = name,
                    loved = loved == 1,
                    lovedAt = null
                )
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun insertScrobble(
            trackId: Long?,
            trackDate: Long?,
            nowPlaying: String?
        ) {
            if (trackId != null && trackDate != null) {
                database.scrobbleQueries.insert(
                    trackId = trackId,
                    listenedAt = trackDate,
                    nowPlaying = nowPlaying == "true"
                )
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = copy()
    }
}