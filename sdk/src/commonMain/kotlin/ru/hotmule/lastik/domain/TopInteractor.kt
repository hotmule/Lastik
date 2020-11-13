package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.Period
import ru.hotmule.lastik.data.remote.entities.TopTracksResponse

enum class Top(
    val id: Long
) {
    Artists(1),
    Albums(2),
    Tracks(3),
}

class TopInteractor(
    private val api: UserApi,
    private val prefs: PrefsStore,
    private val artistQueries: ArtistQueries,
    private val albumQueries: AlbumQueries,
    private val trackQueries: TrackQueries,
    private val periodQueries: PeriodQueries,
    private val statisticQueries: StatisticQueries,
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

    suspend fun refreshArtists(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = artistQueries.getTopArtistsCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            val response = api.getTopArtists(page, getPeriodValue(Top.Artists))

            artistQueries.transaction {

                if (firstPage)
                    statisticQueries.deleteSectionTop(Top.Artists.id)

                response?.top?.artists?.forEach {

                    artistsInteractor.insertArtist(it.name)?.let { artistId ->

                        statisticQueries.insert(
                            sectionId = Top.Artists.id,
                            itemId = artistId,
                            rank = it.attributes?.rank,
                            playCount = it.playCount,
                        )
                    }
                }
            }
        }
    }

    suspend fun refreshAlbums(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = albumQueries.getTopAlbumsCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            val response = api.getTopAlbums(page, getPeriodValue(Top.Albums))

            albumQueries.transaction {

                if (firstPage)
                    statisticQueries.deleteSectionTop(Top.Albums.id)

                response?.top?.albums?.forEach {

                    artistsInteractor.insertArtist(it.artist?.name)?.let { artistId ->

                        it.name?.let { album ->

                            with(albumQueries) {

                                insert(
                                    artistId = artistId,
                                    name = album,
                                    lowArtwork = it.images?.get(2)?.url,
                                    highArtwork = it.images?.get(3)?.url
                                )

                                getId(artistId, album).executeAsOneOrNull()?.let { albumId ->

                                    statisticQueries.insert(
                                        sectionId = Top.Albums.id,
                                        itemId = albumId,
                                        rank = it.attributes?.rank,
                                        playCount = it.playCount,
                                    )
                                }
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
        providePage(
            currentItemsCount = trackQueries.getTopTracksCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            val response = api.getTopTracks(page, getPeriodValue(Top.Tracks))

            trackQueries.transaction {

                if (firstPage)
                    statisticQueries.deleteSectionTop(Top.Tracks.id)

                response?.top?.list?.forEach {

                    artistsInteractor.insertArtist(it.artist?.name)?.let { artistId ->

                        it.name?.let { track ->

                            with(trackQueries) {

                                insert(
                                    artistId = artistId,
                                    name = track,
                                    albumId = null,
                                    loved = false,
                                    lovedAt = null
                                )

                                getId(artistId, track).executeAsOneOrNull()?.let { trackId ->
                                    statisticQueries.insert(
                                        sectionId = Top.Tracks.id,
                                        itemId = trackId,
                                        rank = it.attributes?.rank,
                                        playCount = it.playCount,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPeriodValue(top: Top): String {

        val periodId = periodQueries
            .getPeriodId(prefs.name!!, top.id)
            .executeAsOneOrNull()
            ?: Period.Overall.ordinal

        return Period.values()[periodId.toInt()].value
    }
}