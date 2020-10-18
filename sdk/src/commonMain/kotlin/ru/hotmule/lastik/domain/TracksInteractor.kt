package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class TracksInteractor(
    private val api: UserApi,
    private val db: LastikDatabase,
    private val prefs: PrefsStore
): BaseInteractor(db, prefs) {

    fun observeTopTracks() = db.trackQueries.topTracks().asFlow().mapToList().map { tracks ->
        tracks.map {
            ListItem(
                position = it.rank,
                imageUrl = it.lowArtwork,
                title = it.track,
                subtitle = it.artist,
                scrobbles = it.playCount
            )
        }
    }

    suspend fun refreshTopTracks(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = db.trackQueries.getTopTracksCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getTopTracks(prefs.name, page).also {
                db.transaction {

                    //if (firstPage) db.artistQueries.deleteTopTracks(prefs.name!!)

                    it?.top?.list?.forEach { track ->

                        with (track) {
                            insertArtist(
                                artist?.name
                            )?.let { artistId ->
                                insertTrack(
                                    artistId = artistId,
                                    name = name,
                                    rank = attributes?.rank,
                                    playCount = playCount
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}