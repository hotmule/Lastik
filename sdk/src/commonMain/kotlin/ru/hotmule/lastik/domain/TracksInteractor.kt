package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class TracksInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
): BaseInteractor(db) {

    fun observeTopTracks() = db.trackQueries.topTracks().asFlow().mapToList().map { tracks ->
        tracks.map {
            LibraryListItem(
                position = it.rank,
                imageUrl = it.lowArtwork,
                title = it.track,
                subtitle = it.artist,
                scrobbles = it.playCount
            )
        }
    }

    fun observeLovedTracks() = db.trackQueries.lovedTracks().asFlow().mapToList().map { tracks ->
        tracks.map {
            LibraryListItem(
                imageUrl = it.lowArtwork,
                title = it.track,
                subtitle = it.artist,
                loved = it.loved
            )
        }
    }

    suspend fun refreshTopTracks() {
        api.getTopTracks(prefs.name).also {
            db.transaction {

                db.trackQueries.deleteTopTracks()

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

    suspend fun refreshLovedTracks() {
        api.getLovedTracks(prefs.name).also {
            db.transaction {

                db.trackQueries.deleteLovedTracks()

                it?.loved?.list?.forEach { track ->
                    insertArtist(track.artist?.name)

                    lastArtistId()?.let { artistId ->
                        insertTrack(
                            artistId = artistId,
                            name = track.name,
                            loved = true
                        )
                    }
                }
            }
        }
    }
}