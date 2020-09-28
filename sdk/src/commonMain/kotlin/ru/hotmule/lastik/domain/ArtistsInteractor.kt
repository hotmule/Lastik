package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ArtistsInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeArtists() = db.artistQueries.artistData().asFlow().mapToList()

    suspend fun refreshArtists() {
        api.getTopArtists(prefs.name).also {

            db.transaction {

                db.artistQueries.deleteAll()

                it?.top?.artists?.forEach { artist ->

                    insertArtist(
                        Attrs(
                            artist.name,
                            artist.playCount,
                            artist.images?.get(2)?.url,
                            artist.images?.get(3)?.url
                        )
                    )
                }
            }
        }
    }
}