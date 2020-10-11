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
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = db.artistQueries.getTopArtistsCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getTopArtists(getUserName(), page).also {
                db.transaction {

                    if (firstPage) db.artistQueries.deleteTopArtist(getUserName())

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
}