package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.LastFmItem

class ScrobblesInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeScrobbles() = db.scrobbleQueries.scrobbleData().asFlow().mapToList()

    suspend fun refreshScrobbles() {

        api.getRecentTracks(prefs.name).also { response ->

            db.transaction {

                db.scrobbleQueries.deleteAll()

                response?.recent?.tracks?.forEach { track ->

                    insertArtist(
                        Attrs(name = track.artist?.name)
                    )

                    lastArtistId()?.let { artistId ->

                        insertAlbum(
                            artistId,
                            Attrs(
                                name = track.album?.name,
                                lowResImage = track.images?.get(2)?.url,
                                highResImage = track.images?.get(3)?.url
                            )
                        )

                        lastAlbumId()?.let { albumId ->

                            insertTrack(
                                albumId,
                                Attrs(name = track.name)
                            )

                            lastTrackId()?.let { trackId ->

                                db.scrobbleQueries.insert(
                                    trackId,
                                    track.date?.uts,
                                    track.date?.toSting,
                                    track.attributes?.nowPlaying == true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}