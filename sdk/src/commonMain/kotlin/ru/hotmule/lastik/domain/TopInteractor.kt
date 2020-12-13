 package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.PeriodLength

 enum class TopType(
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
        provideTopPage(
            firstPage = firstPage,
            type = TopType.Artists
        ) { page, period ->
            
            api.getTopArtists(page, PeriodLength.getValue(period?.lengthId)).also {

                artistQueries.transaction {

                    //if (firstPage) statisticQueries.deleteSectionTop(TopType.Artists.id)

                    it?.top?.artists?.forEach {

                        artistsInteractor.insertArtist(it.name)?.let { artistId ->

                            if (period?.id != null && it.attributes?.rank != null) {
                                statisticQueries.insert(
                                    periodId = period.id,
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
    }

    suspend fun refreshAlbums(
        firstPage: Boolean
    ) {
        provideTopPage(
            firstPage = firstPage,
            type = TopType.Albums
        ) { page, period ->

            api.getTopAlbums(page, PeriodLength.getValue(period?.lengthId)).also {

                albumQueries.transaction {

                    //if (firstPage) statisticQueries.deleteSectionTop(TopType.Albums.id)

                    it?.top?.albums?.forEach {

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

                                        if (period?.id != null && it.attributes?.rank != null) {
                                            statisticQueries.insert(
                                                periodId = period.id,
                                                itemId = albumId,
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
            }
        }
    }

    suspend fun refreshTopTracks(
        firstPage: Boolean
    ) {
        provideTopPage(
            firstPage = firstPage,
            type = TopType.Tracks
        ) { page, period ->

            api.getTopTracks(page, PeriodLength.getValue(period?.lengthId)).also {

                trackQueries.transaction {

                    //if (firstPage) statisticQueries.deleteSectionTop(TopType.Tracks.id)

                    it?.top?.list?.forEach {

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

                                        if (period?.id != null && it.attributes?.rank != null) {
                                            statisticQueries.insert(
                                                periodId = period.id,
                                                itemId = trackId,
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
            }
        }
    }

    private suspend fun provideTopPage(
        firstPage: Boolean,
        type: TopType,
        loadPage: suspend (Int, Period?) -> Unit
    ) {

        val currentItemsCountQuery = when (type) {
            TopType.Artists -> artistQueries.getTopArtistsCount()
            TopType.Albums -> albumQueries.getTopAlbumsCount()
            TopType.Tracks -> trackQueries.getTopTracksCount()
        }

        var period: Period? = null

        with (periodQueries) {

            transaction {

                period = getPeriod(prefs.name!!, type.id).executeAsOneOrNull()

                if (period == null) {

                    periodQueries.upsert(
                        username = prefs.name!!,
                        lengthId = PeriodLength.Overall.ordinal.toLong(),
                        topTypeId = type.id
                    )

                    period = getPeriod(prefs.name!!, type.id).executeAsOneOrNull()
                }
            }
        }

        providePage(
            currentItemsCount = currentItemsCountQuery.executeAsOne().toInt(),
            firstPage = firstPage
        ) {
            loadPage.invoke(it, period)
        }
    }
}