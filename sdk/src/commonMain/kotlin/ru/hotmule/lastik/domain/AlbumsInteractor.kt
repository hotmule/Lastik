package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.UserApi

class AlbumsInteractor(
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeAlbums() = db.albumQueries.albumTop().asFlow().mapToList().map { albums ->
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
            currentItemsCount = db.albumQueries.getTopAlbumsCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getTopAlbums(getUserName(), page).also {
                db.transaction {

                    if (firstPage) db.artistQueries.deleteTopAlbums(getUserName())

                    it?.top?.albums?.forEach { album ->

                        insertArtist(album.artist?.name)
                        lastArtistId()?.let { artistId ->
                            insertAlbum(
                                artistId,
                                album.name,
                                album.images?.get(2)?.url,
                                album.images?.get(3)?.url,
                                Stat(
                                    album.attributes?.rank,
                                    album.playCount
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}