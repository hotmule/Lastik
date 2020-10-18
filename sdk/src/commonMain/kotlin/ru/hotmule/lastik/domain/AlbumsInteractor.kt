package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class AlbumsInteractor(
    private val api: UserApi,
    private val db: LastikDatabase,
    private val prefs: PrefsStore
) : BaseInteractor(db, prefs) {

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

            api.getTopAlbums(prefs.name, page).also {
                db.transaction {

                    //if (firstPage) db.artistQueries.deleteTopAlbums(prefs.name!!)

                    it?.top?.albums?.forEach { album ->
                        with (album) {
                            artist?.name?.let { artist ->
                                insertArtist(artist)?.let { artistId ->
                                    insertAlbum(
                                        artistId,
                                        name,
                                        images?.get(2)?.url,
                                        images?.get(3)?.url,
                                        attributes?.rank,
                                        playCount
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