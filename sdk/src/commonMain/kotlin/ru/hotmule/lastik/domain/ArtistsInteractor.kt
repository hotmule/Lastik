package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.UserApi

class ArtistsInteractor(
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeArtists() = db.artistQueries.artistTop(getUserName())
        .asFlow()
        .mapToList()
        .map { artists ->
            artists.map {
                ListItem(
                    title = it.name,
                    position = it.rank,
                    scrobbles = it.playCount
                )
            }
        }

    suspend fun refreshArtists(
        cleanOld: Boolean
    ) {
        api.getTopArtists(getUserName()).also {
            db.transaction {
                db.artistQueries.deleteTopArtist(getUserName())
                it?.top?.artists?.forEach { artist ->
                    insertArtist(
                        artist.name,
                        Stat(
                            artist.attributes?.rank,
                            artist.playCount
                        )
                    )
                }
            }
        }
    }
}