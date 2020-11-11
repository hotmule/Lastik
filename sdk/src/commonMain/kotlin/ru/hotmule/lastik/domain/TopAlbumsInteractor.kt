package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.AlbumQueries
import ru.hotmule.lastik.data.local.StatisticQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class TopAlbumsInteractor(
    private val api: UserApi,
    private val albumQueries: AlbumQueries,
    private val statisticQueries: StatisticQueries,
    private val artistsInteractor: ArtistsInteractor
) : BaseInteractor() {

    fun observeAlbums() = albumQueries.albumTop().asFlow().mapToList().map { albums ->
        albums.map {
            ListItem(
                position = it.rank,
                imageUrl = it.lowArtwork,
                title = it.album,
                subtitle = it.artist,
                scrobbles = it.playCount
            )
        }
    }

    suspend fun refreshAlbums(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = albumQueries.getTopAlbumsCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getTopAlbums(page).also {
                albumQueries.transaction {

                    if (firstPage) statisticQueries.deleteSectionTop(Section.ALBUMS.id)

                    it?.top?.albums?.forEach { album ->
                        with(album) {
                            artist?.name?.let { artist ->
                                artistsInteractor.insertArtist(artist)?.let { artistId ->
                                    name?.let { album ->
                                        with (albumQueries) {
                                            insert(
                                                artistId = artistId,
                                                name = album,
                                                lowArtwork = images?.get(2)?.url,
                                                highArtwork = images?.get(3)?.url
                                            )
                                            getId(artistId, album)
                                                .executeAsOneOrNull()?.let { albumId ->
                                                    statisticQueries.insert(
                                                        sectionId = Section.ALBUMS.id,
                                                        itemId = albumId,
                                                        rank = attributes?.rank,
                                                        playCount = playCount,
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
    }
}