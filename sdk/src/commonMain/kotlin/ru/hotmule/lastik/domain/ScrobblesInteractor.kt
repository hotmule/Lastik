package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ScrobblesInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeScrobbles() =
        db.scrobbleQueries.scrobbleData().asFlow().mapToList().map { scrobbles ->
            scrobbles.map {
                ListItem(
                    time = it.time,
                    title = it.track,
                    subtitle = it.artist,
                    imageUrl = it.lowArtwork,
                    nowPlaying = it.nowPlaying
                )
            }
        }

    suspend fun refreshScrobbles() {
        api.getRecentTracks(prefs.name).also { response ->
            db.transaction {

                db.scrobbleQueries.deleteScrobbles()

                response?.recent?.tracks?.forEach { track ->

                    insertArtist(track.artist?.text)

                    lastArtistId()?.let { artistId ->
                        insertAlbum(
                            artistId,
                            track.album?.text,
                            track.images?.get(2)?.url,
                            track.images?.get(3)?.url
                        )

                        lastAlbumId()?.let { albumId ->
                            insertTrack(
                                artistId,
                                albumId,
                                track.name
                            )

                            lastTrackId()?.let { trackId ->
                                db.scrobbleQueries.insert(
                                    trackId,
                                    track.date?.uts,
                                    track.date?.toSting,
                                    track.attributes?.nowPlaying == "true"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}