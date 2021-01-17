package ru.hotmule.lastik.domain

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

enum class TopType {
    Artists,
    Albums,
    Tracks;
}

enum class TopPeriod(
    val value: String
) {
    Overall("overall"),
    Week("7day"),
    Month("1month"),
    YearQuarter("3month"),
    YearHalf("6month"),
    Year("12month");
}

class TopInteractor(
    private val api: UserApi,
    private val prefs: PrefsStore,
    private val artistQueries: ArtistQueries,
    private val albumQueries: AlbumQueries,
    private val trackQueries: TrackQueries,
    private val topQueries: TopQueries,
    private val artistsInteractor: ArtistsInteractor
) : BaseInteractor() {

    fun observeArtists() = artistQueries.artistTop()
        .asFlow()
        .mapToList()
        .map { artists ->
            artists.map {
                ListItem(
                    title = it.name,
                    rank = it.rank,
                    imageUrl = it.lowArtwork,
                    playCount = it.playCount,
                )
            }
        }

    fun observeAlbums() = albumQueries.albumTop()
        .asFlow()
        .mapToList()
        .map { albums ->
            albums.map {
                ListItem(
                    rank = it.rank,
                    imageUrl = it.lowArtwork,
                    title = it.album,
                    subtitle = it.artist,
                    playCount = it.playCount
                )
            }
        }

    fun observeTopTracks() = trackQueries.topTracks()
        .asFlow()
        .mapToList()
        .map { tracks ->
            tracks.map {
                ListItem(
                    rank = it.rank,
                    imageUrl = it.lowArtwork,
                    title = it.track,
                    subtitle = it.artist,
                    playCount = it.playCount
                )
            }
        }

    fun observeTopPeriodId(type: TopType) = when (type) {
        TopType.Artists -> prefs.topArtistsPeriod
        TopType.Albums -> prefs.topAlbumsPeriod
        TopType.Tracks -> prefs.topTracksPeriod
    }

    fun updateTopPeriod(
        type: TopType,
        period: TopPeriod
    ) {
        when (type) {
            TopType.Artists -> prefs.topArtistsPeriodId = period.ordinal
            TopType.Albums -> prefs.topAlbumsPeriodId = period.ordinal
            TopType.Tracks -> prefs.topTracksPeriodId = period.ordinal
        }
    }

    suspend fun refreshArtists(
        firstPage: Boolean
    ) {
        provideTopPage(
            firstPage = firstPage,
            type = TopType.Artists,
            loadPage = api::getTopArtists
        ) { response, type, period ->

            artistQueries.transaction {

                //if (firstPage) statisticQueries.deleteSectionTop(TopType.Artists.id)

                response?.top?.artists?.forEach {
                    if (it.attributes?.rank != null) {

                        artistsInteractor.insertArtist(it.name)?.let { artistId ->
                            topQueries.insert(
                                type = type,
                                period = period,
                                itemId = artistId,
                                rank = it.attributes.rank,
                                playCount = it.playCount,
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun refreshAlbums(
        firstPage: Boolean
    ) {
        provideTopPage(
            firstPage = firstPage,
            type = TopType.Albums,
            loadPage = api::getTopAlbums
        ) { response, type, period ->

            albumQueries.transaction {

                //if (firstPage) statisticQueries.deleteSectionTop(TopType.Albums.id)

                response?.top?.albums?.forEach {
                    if (it.name != null && it.attributes?.rank != null) {

                        artistsInteractor.insertArtist(it.artist?.name)?.let { artistId ->
                            albumQueries.insert(
                                artistId = artistId,
                                name = it.name,
                                lowArtwork = it.images?.get(2)?.url,
                                highArtwork = it.images?.get(3)?.url
                            )

                            albumQueries.getId(artistId, it.name).executeAsOneOrNull()?.let { id ->
                                topQueries.insert(
                                    type = type,
                                    period = period,
                                    itemId = id,
                                    rank = it.attributes.rank,
                                    playCount = it.playCount,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun refreshTopTracks(
        firstPage: Boolean
    ) {
        provideTopPage(
            firstPage = firstPage,
            type = TopType.Tracks,
            loadPage = api::getTopTracks
        ) { response, type, period ->

            trackQueries.transaction {

                //if (firstPage) statisticQueries.deleteSectionTop(TopType.Tracks.id)

                response?.top?.list?.forEach {
                    if (it.name != null && it.attributes?.rank != null) {

                        artistsInteractor.insertArtist(it.artist?.name)?.let { artistId ->
                            trackQueries.insert(
                                artistId = artistId,
                                name = it.name,
                                albumId = null,
                                loved = false,
                                lovedAt = null
                            )

                            trackQueries.getId(artistId, it.name).executeAsOneOrNull()?.let { id ->
                                topQueries.insert(
                                    type = type,
                                    period = period,
                                    itemId = id,
                                    rank = it.attributes.rank,
                                    playCount = it.playCount,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun <T> provideTopPage(
        firstPage: Boolean,
        type: TopType,
        loadPage: suspend (Int, String) -> T?,
        onResponse: suspend (T?, TopType, TopPeriod) -> Unit
    ) {

        val currentItemsCountQuery: Query<Long>
        val periodId: Int

        when (type) {
            TopType.Artists -> {
                currentItemsCountQuery = artistQueries.getTopArtistsCount()
                periodId = prefs.topArtistsPeriodId
            }
            TopType.Albums -> {
                currentItemsCountQuery = albumQueries.getTopAlbumsCount()
                periodId = prefs.topAlbumsPeriodId
            }
            TopType.Tracks -> {
                currentItemsCountQuery = trackQueries.getTopTracksCount()
                periodId = prefs.topArtistsPeriodId
            }
        }

        providePage(
            currentItemsCount = currentItemsCountQuery.executeAsOne().toInt(),
            firstPage = firstPage
        ) {

            val period = TopPeriod.values()[periodId]

            loadPage.invoke(it, period.value).also { response ->
                onResponse.invoke(response, type, period)
            }
        }
    }
}