package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ArtistsInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeArtists() = db.artistQueries.artistTop().asFlow().mapToList().map { artists ->
        artists.map {
            LibraryListItem(
                title = it.name,
                position = it.rank,
                scrobbles = it.playCount,
                imageUrl = it.lowResImage
            )
        }
    }

    suspend fun refreshArtists() {
        api.getTopArtists(prefs.name).also {
            db.transaction {
                db.artistQueries.deleteArtistTop()
                it?.top?.artists?.forEach { artist ->
                    insertArtist(
                        Attrs(
                            name = artist.name,
                            rank = artist.attributes?.rank,
                            playCount = artist.playCount,
                            lowResImage = artist.images?.get(2)?.url,
                            highResImage = artist.images?.get(3)?.url
                        )
                    )
                }
            }
        }
    }
}