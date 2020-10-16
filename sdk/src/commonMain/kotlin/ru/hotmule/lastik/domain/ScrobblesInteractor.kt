package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.remote.api.UserApi

class ScrobblesInteractor(
    private val api: UserApi,
    private val db: LastikDatabase
) : BaseInteractor(db) {

    fun observeScrobbles() = db.scrobbleQueries.scrobbleData(getUserName())
        .asFlow()
        .mapToList()
        .map { scrobbles ->
            scrobbles.map {
                ListItem(
                    title = it.track,
                    loved = it.loved,
                    time = it.listenedAt,
                    subtitle = it.artist,
                    imageUrl = it.lowArtwork,
                    nowPlaying = it.nowPlaying
                )
            }
        }

    suspend fun loadScrobbles(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = db.scrobbleQueries.getScrobblesCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getRecentTracks(getUserName(), page).also { response ->

                db.transaction {

                    if (firstPage) db.artistQueries.deleteScrobbles(getUserName())

                    response?.recent?.tracks?.forEach { track ->

                        insertArtist(
                            track.artist?.name
                        )?.let { artistId ->

                            insertAlbum(
                                artistId,
                                track.album?.text,
                                track.images?.get(2)?.url,
                                track.images?.get(3)?.url
                            )?.let { albumId ->

                                insertTrack(
                                    artistId,
                                    albumId,
                                    track.name,
                                    track.loved == 1
                                )?.let { trackId ->

                                    db.scrobbleQueries.insert(
                                        trackId,
                                        track.date?.uts,
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
}