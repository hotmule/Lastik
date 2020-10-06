package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.UserApi

class TracksInteractor(
    private val api: UserApi,
    private val db: LastikDatabase
): BaseInteractor(db) {

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

    suspend fun refreshTopTracks() {
        api.getTopTracks(getUserName()).also {
            db.transaction {

                db.artistQueries.deleteTopTracks(getUserName())

                it?.top?.list?.forEach { track ->
                    insertArtist(track.artist?.name)

                    lastArtistId()?.let { artistId ->
                        insertTrack(
                            artistId = artistId,
                            name = track.name,
                            stat = Stat(
                                track.attributes?.rank,
                                track.playCount
                            )
                        )
                    }
                }
            }
        }
    }
}