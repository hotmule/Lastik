package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ScrobblesInteractor(
    private val api: UserApi,
    private val db: LastikDatabase,
    private val prefs: PrefsStore
) : BaseInteractor(db, prefs) {

    fun observeScrobbles() = db.scrobbleQueries.scrobbleData(prefs.name!!)
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

            api.getRecentTracks(prefs.name, page).also { response ->

                db.transaction {

                    //if (firstPage) db.artistQueries.deleteScrobbles(prefs.name!!)

                    response?.recent?.tracks?.forEach { track ->

                        with(track) {

                            if (date?.uts != null) {

                                insertArtist(
                                    artist?.name
                                )?.let { artistId ->

                                    insertAlbum(
                                        artistId,
                                        album?.text,
                                        images?.get(2)?.url,
                                        images?.get(3)?.url
                                    )?.let { albumId ->

                                        insertTrack(
                                            artistId,
                                            albumId,
                                            name,
                                            loved == 1
                                        )?.let { trackId ->

                                            db.scrobbleQueries.insert(
                                                trackId,
                                                date.uts,
                                                attributes?.nowPlaying == "true"
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
    }
}