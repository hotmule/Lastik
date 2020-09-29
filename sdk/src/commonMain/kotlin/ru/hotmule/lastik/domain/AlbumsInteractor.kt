package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class AlbumsInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeAlbums() = db.albumQueries.albumTop().asFlow().mapToList().map { albums ->
        albums.map {
            LibraryListItem(
                position = it.rank,
                imageUrl = it.lowResImage,
                title = it.album,
                subtitle = it.artist,
                scrobbles = it.playCount
            )
        }
    }

    suspend fun refreshAlbums() {

        api.getTopAlbums(prefs.name).also {

            db.transaction {

                db.albumQueries.deleteAlbumTop()

                it?.top?.albums?.forEach { album ->

                    insertArtist(
                        Attrs(name = album.artist?.name)
                    )

                    lastArtistId()?.let {
                        insertAlbum(
                            it,
                            Attrs(
                                name = album.name,
                                rank = album.attributes?.rank,
                                playCount = album.playCount,
                                lowResImage = album.images?.get(2)?.url,
                                highResImage = album.images?.get(3)?.url
                            )
                        )
                    }
                }
            }
        }
    }
}