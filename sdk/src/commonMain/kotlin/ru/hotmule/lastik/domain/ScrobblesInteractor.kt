package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ScrobblesInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val db: LastikDatabase
) {

    //fun observeScrobbles() = db.selectAll().asFlow().mapToList()

    suspend fun refreshScrobbles() {

        api.getRecentTracks(prefs.name).also { response ->
            response?.recent?.tracks?.let {

                val artists = it.mapNotNull { track ->
                    track.artist?.id?.let {
                        Artist(
                            track.artist.id,
                            track.artist.name
                        )
                    }
                }

                val albums = it.mapNotNull { track ->
                    track.artist?.id?.let {
                        track.album?.id?.let {
                            Album(
                                track.album.id,
                                track.artist.id,
                                track.images?.get(1)?.url,
                                track.images?.get(3)?.url,
                                track.album.name
                            )
                        }
                    }
                }

                val tracks = it.mapNotNull { track ->
                    track.artist?.id?.let {
                        track.album?.id?.let {
                            track.id?.let {
                                Track(
                                    track.id,
                                    track.artist.id,
                                    track.album.id,
                                    track.name,
                                    false
                                )
                            }
                        }
                    }
                }

                val scrobbles = it.mapNotNull { track ->
                    track.id?.let {
                        Scrobble(
                            track.id,
                            track.date?.uts,
                            track.attributes?.nowPlaying == true
                        )
                    }
                }

                db.transaction {
                    artists.forEach { artist -> db.artistQueries.insert(artist) }
                    albums.forEach { album -> db.albumQueries.insert(album) }
                    tracks.forEach { track -> db.trackQueries.insert(track) }
                    scrobbles.forEach { scrobble -> db.scrobbleQueries.insert(scrobble) }
                }
            }
        }
    }
}