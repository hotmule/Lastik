package ru.hotmule.lastik.feature.shelf.store

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.Date
import ru.hotmule.lastik.data.remote.entities.Image
import ru.hotmule.lastik.data.remote.entities.LibraryItem
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.Formatter

internal class ShelfStoreRepository(
    private val database: LastikDatabase,
    private val prefsStore: PrefsStore,
    private val trackApi: TrackApi,
    private val userApi: UserApi,
    private val index: Int
) : ShelfStore.Repository {

    override val items: Flow<List<ShelfItem>> = when (index) {
        0 -> scrobblesFlow()
        1 -> topArtistsFlow()
        2 -> topAlbumsFlow()
        3 -> topTracksFlow()
        else -> lovedTracksFlow()
    }

    override suspend fun getItemsCount(): Int = withContext(AppCoroutineDispatcher.IO) {
        when (index) {
            0 -> database.scrobbleQueries.getScrobblesCount()
            1, 2, 3 -> database.topQueries.getTopCount(index.toLong(), getShelfPeriod(index).toLong())
            else -> database.trackQueries.getLovedTracksPageCount()
        }.executeAsOne().toInt()
    }

    override suspend fun provideItems(page: Int) {
        withContext(AppCoroutineDispatcher.IO) {

            val items = with(userApi) {
                when (index) {
                    0 -> getScrobbles(page)?.recent?.tracks
                    1 -> getTopArtists(page)?.top?.artists
                    2 -> getTopAlbums(page)?.top?.albums
                    3 -> getTopTracks(page)?.top?.tracks
                    else -> getLovedTracks(page)?.loved?.tracks
                }
            }

            database.transaction {
                if (page == 1) clearItems()
                items?.forEach { saveItem(it) }
            }
        }
    }

    private fun clearItems() {
        when (index) {
            0 -> database.scrobbleQueries.deleteAll()
            1, 2, 3 -> database.topQueries.deleteTop(index.toLong(), getShelfPeriod(index).toLong())
            4 -> database.trackQueries.dropLovedTrackDates()
        }
    }

    private fun saveItem(item: LibraryItem) {
        when (index) {
            0 -> saveScrobble(item)
            1 -> saveTopArtist(item)
            2 -> saveTopAlbum(item)
            3 -> saveTopTrack(item)
            4 -> saveLovedTrack(item)
        }
    }

    override suspend fun setTrackLove(artist: String, track: String, isLoved: Boolean) {
        withContext(AppCoroutineDispatcher.IO) {
            trackApi.setLoved(track, artist, isLoved)
            database.trackQueries.updateTrackLove(isLoved, track, artist)
        }
    }

    private fun scrobblesFlow() = database.scrobbleQueries.scrobbleData()
        .asFlow()
        .mapToList(AppCoroutineDispatcher.IO)
        .map { scrobbles ->
            scrobbles.map {
                ShelfItem(
                    highlighted = it.nowPlaying,
                    image = it.lowArtwork ?: UserApi.defaultImageUrl,
                    title = it.track ?: "",
                    subtitle = it.artist,
                    loved = it.loved,
                    hint = if (it.listenedAt != 0L)
                        Formatter.utsDateToString(it.listenedAt, "d MMM, HH:mm")
                    else
                        "Scrobbling now",
                )
            }
        }

    private fun topArtistsFlow() = prefsStore.getTopPeriodAsFlow(index).flatMapLatest { period ->
        database.topQueries.artistTop(period.toLong())
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { artists ->
                artists.map {
                    ShelfItem(
                        image = it.lowArtwork ?: UserApi.defaultImageUrl,
                        title = it.name ?: "",
                        hint = "${Formatter.numberToCommasString(it.playCount)} scrobbles",
                        rank = it.rank,
                        playCount = it.playCount,
                    )
                }
            }
    }

    private fun topAlbumsFlow() = prefsStore.getTopPeriodAsFlow(index).flatMapLatest { period ->
        database.topQueries.albumTop(period.toLong())
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { albums ->
                albums.map {
                    ShelfItem(
                        image = it.lowArtwork ?: UserApi.defaultImageUrl,
                        title = it.album ?: "",
                        hint = "${Formatter.numberToCommasString(it.playCount)} scrobbles",
                        subtitle = it.artist,
                        rank = it.rank,
                        playCount = it.playCount
                    )
                }
            }
    }

    private fun topTracksFlow() = prefsStore.getTopPeriodAsFlow(index).flatMapLatest { period ->
        database.topQueries.trackTop(period.toLong())
            .asFlow()
            .mapToList(AppCoroutineDispatcher.IO)
            .map { tracks ->
                tracks.map {
                    ShelfItem(
                        image = it.lowArtwork ?: UserApi.defaultImageUrl,
                        title = it.track ?: "",
                        hint = "${Formatter.numberToCommasString(it.playCount)} scrobbles",
                        subtitle = it.artist,
                        rank = it.rank,
                        playCount = it.playCount
                    )
                }
            }
    }

    private fun lovedTracksFlow() = database.trackQueries.lovedTracks()
        .asFlow()
        .mapToList(AppCoroutineDispatcher.IO)
        .map { tracks ->
            tracks.map {
                ShelfItem(
                    image = it.lowArtwork ?: UserApi.defaultImageUrl,
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
        insertTop(index, getShelfPeriod(index).toLong(), artistId, artist.attributes?.rank, artist.playCount)
    }

    private fun saveTopAlbum(album: LibraryItem) {
        val artistId = insertArtist(album.artist?.name)
        val albumId = insertAlbum(artistId, album.name, album.images)
        insertTop(index, getShelfPeriod(index).toLong(), albumId, album.attributes?.rank, album.playCount)
    }

    private fun saveTopTrack(track: LibraryItem) {
        val artistId = insertArtist(track.artist?.name)
        val trackId = insertTrack(artistId, track.name)
        insertTop(index, getShelfPeriod(index).toLong(), trackId, track.attributes?.rank, track.playCount)
    }

    private fun saveLovedTrack(track: LibraryItem) {
        val artistId = insertArtist(track.artist?.name)
        upsertLovedTrack(artistId, track.name, track.date?.uts)
    }

    private fun getShelfPeriod(shelfIndex: Int) = when (shelfIndex) {
        1 -> prefsStore.artistsPeriod
        2 -> prefsStore.albumsPeriod
        else -> prefsStore.tracksPeriod
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