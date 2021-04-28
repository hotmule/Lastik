package ru.hotmule.lastik.feature.shelf.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.Date
import ru.hotmule.lastik.data.remote.entities.Image
import ru.hotmule.lastik.data.remote.entities.LibraryItem
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent.ShelfItem
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.Formatter

internal class ShelfStoreFactory(
    private val storeFactory: StoreFactory,
    private val database: LastikDatabase,
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val index: Int,
    private val period: Long = prefs.getShelfPeriod(index).toLong()
) {

    fun create(): ShelfStore = object : ShelfStore, Store<Intent, State, Nothing> by storeFactory.create(
        name = "${ShelfStore::class.simpleName} $index",
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = when (result) {
            is Result.ItemsReceived -> copy(items = result.items)
            is Result.Loading -> copy(
                isRefreshing = result.isLoading && result.isFirstPage,
                isMoreLoading = result.isLoading && !result.isFirstPage
            )
        }
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {

        override suspend fun executeAction(
            action: Unit,
            getState: () -> State
        ) {
            withContext(AppCoroutineDispatcher.IO) {
                launch { collectItems() }
                launch { loadPage(true) }
            }
        }

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {
            when (intent) {
                Intent.RefreshItems -> loadPage(true)
                Intent.LoadMoreItems -> loadPage(false)
                is Intent.MakeLove -> {
                }
            }
        }

        private suspend fun collectItems() {

            val itemsFlow = when (index) {
                0 -> scrobblesFlow()
                1 -> topArtistsFlow()
                2 -> topAlbumsFlow()
                3 -> topTracksFlow()
                else -> lovedTracksFlow()
            }

            itemsFlow.collect {
                withContext(AppCoroutineDispatcher.Main) {
                    dispatch(Result.ItemsReceived(it))
                }
            }
        }

        private suspend fun loadPage(isFirst: Boolean) {
            withContext(AppCoroutineDispatcher.IO) {

                try {

                    withContext(AppCoroutineDispatcher.Main) {
                        dispatch(Result.Loading(isLoading = true, isFirstPage = isFirst))
                    }

                    val periodName = arrayOf(
                        "overall", "7day", "1month", "3month", "6month", "12month"
                    )[period.toInt()]

                    with(database) {

                        val count = when (index) {
                            0 -> scrobbleQueries.getScrobblesCount()
                            1, 2, 3 -> topQueries.getTopCount(index.toLong(), period)
                            else -> trackQueries.getLovedTracksPageCount()
                        }.executeAsOne().toInt()

                        if (isFirst || count.rem(50) == 0) {

                            val page = if (isFirst) 1 else count / 50 + 1
                            val items: List<LibraryItem>?
                            val save: (LibraryItem) -> Unit

                            when (index) {
                                0 -> {
                                    items = api.getScrobbles(page)?.recent?.tracks
                                    save = ::saveScrobble
                                }
                                1 -> {
                                    items = api.getTopArtists(page, periodName)?.top?.artists
                                    save = ::saveTopArtist
                                }
                                2 -> {
                                    items = api.getTopAlbums(page, periodName)?.top?.albums
                                    save = ::saveTopAlbum
                                }
                                3 -> {
                                    items = api.getTopTracks(page, periodName)?.top?.tracks
                                    save = ::saveTopTrack
                                }
                                else -> {
                                    items = api.getLovedTracks(page)?.loved?.tracks
                                    save = ::saveLovedTrack
                                }
                            }

                            transaction {

                                if (isFirst) {
                                    when (index) {
                                        0 -> scrobbleQueries.deleteAll()
                                        1, 2, 3 -> topQueries.deleteTop(index.toLong(), period)
                                        4 -> trackQueries.dropLovedTrackDates()
                                    }
                                }

                                items?.forEach { item ->
                                    save(item)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    withContext(AppCoroutineDispatcher.Main) {
                        dispatch(Result.Loading(isLoading = false, isFirstPage = isFirst))
                    }
                }
            }
        }

        private fun scrobblesFlow() = database.scrobbleQueries.scrobbleData()
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { scrobbles ->
                scrobbles.map {
                    ShelfItem(
                        highlighted = it.nowPlaying,
                        image = it.lowArtwork ?: ShelfComponent.defaultImageUrl,
                        title = it.track ?: ShelfComponent.defaultTitle,
                        subtitle = it.artist,
                        loved = it.loved,
                        hint = if (it.listenedAt != 0L)
                            Formatter.utsDateToString(it.listenedAt, "d MMM, HH:mm")
                        else
                            "Scrobbling now",
                    )
                }
            }

        private fun topArtistsFlow() = database.topQueries.artistTop(period)
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { artists ->
                artists.map {
                    ShelfItem(
                        image = it.lowArtwork ?: ShelfComponent.defaultImageUrl,
                        title = it.name ?: ShelfComponent.defaultTitle,
                        hint = "${Formatter.numberToCommasString(it.playCount)} scrobbles",
                        rank = it.rank,
                        playCount = it.playCount,
                    )
                }
            }

        private fun topAlbumsFlow() = database.topQueries.albumTop(period)
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { albums ->
                albums.map {
                    ShelfItem(
                        image = it.lowArtwork ?: ShelfComponent.defaultImageUrl,
                        title = it.album ?: ShelfComponent.defaultTitle,
                        hint = "${Formatter.numberToCommasString(it.playCount)} scrobbles",
                        subtitle = it.artist,
                        rank = it.rank,
                        playCount = it.playCount
                    )
                }
            }

        private fun topTracksFlow() = database.topQueries.trackTop(period)
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { tracks ->
                tracks.map {
                    ShelfItem(
                        image = it.lowArtwork ?: ShelfComponent.defaultImageUrl,
                        title = it.track ?: ShelfComponent.defaultTitle,
                        hint = "${Formatter.numberToCommasString(it.playCount)} scrobbles",
                        subtitle = it.artist,
                        rank = it.rank,
                        playCount = it.playCount
                    )
                }
            }

        private fun lovedTracksFlow() = database.trackQueries.lovedTracks()
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { tracks ->
                tracks.map {
                    ShelfItem(
                        image = it.lowArtwork ?: ShelfComponent.defaultImageUrl,
                        title = it.track,
                        subtitle = it.artist,
                        hint = Formatter.utsDateToString(it.lovedAt, "d MMM, HH:mm"),
                        loved = it.loved
                    )
                }
            }

        private fun saveScrobble(scrobble: LibraryItem) {

            if (scrobble.date?.uts == null && scrobble.attributes?.nowPlaying == "true")
                scrobble.date = Date(0)

            val artistId = insertArtist(scrobble.artist?.name)
            val albumId = insertAlbum(artistId, scrobble.album?.text, scrobble.images)
            val trackId = upsertScrobbleTrack(artistId, albumId, scrobble.name, scrobble.loved)
            insertScrobble(trackId, scrobble.date?.uts, scrobble.attributes?.nowPlaying)
        }

        private fun saveTopArtist(artist: LibraryItem) {
            val artistId = insertArtist(artist.name)
            insertTop(index, period, artistId, artist.attributes?.rank, artist.playCount)
        }

        private fun saveTopAlbum(album: LibraryItem) {
            val artistId = insertArtist(album.artist?.name)
            val albumId = insertAlbum(artistId, album.name, album.images)
            insertTop(index, period, albumId, album.attributes?.rank, album.playCount)
        }

        private fun saveTopTrack(track: LibraryItem) {
            val artistId = insertArtist(track.artist?.name)
            val trackId = insertTrack(artistId, track.name)
            insertTop(index, period, trackId, track.attributes?.rank, track.playCount)
        }

        private fun saveLovedTrack(track: LibraryItem) {
            val artistId = insertArtist(track.artist?.name)
            upsertLovedTrack(artistId, track.name, track.date?.uts)
        }

        private fun insertScrobble(
            trackId: Long?,
            trackDate: Long?,
            nowPlaying: String?
        ) {
            if (trackId != null && trackDate != null) {
                database.scrobbleQueries.insert(trackId, trackDate, nowPlaying == "true")
            }
        }

        private fun insertTop(
            index: Int,
            period: Long,
            itemId: Long?,
            rank: Int?,
            playCount: Long?
        ) {
            if (itemId != null && rank != null) {
                database.topQueries.insert(index.toLong(), period, rank, itemId, playCount)
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
                insert(artistId, name, images?.get(2)?.url, images?.get(3)?.url)
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun insertTrack(
            artistId: Long?,
            name: String?
        ) = if (artistId != null && name != null) {
            with(database.trackQueries) {
                insert(artistId, null, name, false, null)
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun upsertScrobbleTrack(
            artistId: Long?,
            albumId: Long?,
            name: String?,
            loved: Int?
        ) = if (artistId != null && name != null) {
            with(database.trackQueries) {
                upsertRecentTrack(albumId, loved == 1, artistId, name, null)
                getId(artistId, name).executeAsOneOrNull()
            }
        } else null

        private fun upsertLovedTrack(
            artistId: Long?,
            name: String?,
            lovedAt: Long?
        ) {
            if (artistId != null && name != null) {
                database.trackQueries.upsertLovedTrack(true, lovedAt, artistId, name, null)
            }
        }
    }
}