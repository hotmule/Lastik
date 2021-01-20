package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.domain.utils.providePage

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
    private val albumQueries: AlbumQueries,
    private val trackQueries: TrackQueries,
    private val topQueries: TopQueries,
    private val artistsInteractor: ArtistsInteractor
) {

    fun observeArtists() = topQueries.artistTop(prefs.getTopPeriod(TopType.Artists))
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

    fun observeAlbums() = topQueries.albumTop(prefs.getTopPeriod(TopType.Albums))
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

    fun observeTopTracks() = topQueries.trackTop(prefs.getTopPeriod(TopType.Tracks))
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

    fun observeTopPeriodId(type: TopType) = prefs.getTopPeriodId(type)

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
        isFirstPage: Boolean
    ) {
        provideTopPage(
            isFirstPage,
            TopType.Artists,
            api::getTopArtists
        ) { response, type, period ->

            response.top?.artists?.forEach {
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

    suspend fun refreshAlbums(
        isFirstPage: Boolean
    ) {
        provideTopPage(
            isFirstPage,
            TopType.Albums,
            api::getTopAlbums
        ) { response, type, period ->

            response.top?.albums?.forEach {
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

    suspend fun refreshTopTracks(
        isFirstPage: Boolean,
    ) {
        provideTopPage(
            isFirstPage,
            TopType.Tracks,
            api::getTopTracks
        ) { response, type, period ->

            response.top?.list?.forEach {
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

    private suspend fun <T> provideTopPage(
        isFirstPage: Boolean,
        type: TopType,
        loadPage: suspend (Int, String) -> T?,
        onResponse: (T, TopType, TopPeriod) -> Unit
    ) {
        prefs.getTopPeriod(type).also { period ->
            providePage(
                isFirstPage,
                topQueries.getTopCount(type, period),
                { loadPage.invoke(it, period.value) },
                { topQueries.deleteTop(type, period) },
                {
                    topQueries.transaction {
                        onResponse.invoke(it, type, period)
                    }
                }
            )
        }
    }
}