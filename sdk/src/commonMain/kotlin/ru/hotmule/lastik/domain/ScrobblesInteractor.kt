package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.LibraryItem

class ScrobblesInteractor(
    private val api: UserApi,
    private val db: LastikDatabase,
    private val prefs: PrefsStore
) : BaseInteractor(db) {

    fun observeScrobbles() = db.scrobbleQueries.scrobbleData()
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

                    response?.recent?.tracks?.forEach {
                        insertRecentTrack(it)
                    }
                }
            }
        }
    }

    private fun insertRecentTrack(track: LibraryItem) {

        with(track) {

            date?.uts?.let { date ->
                insertArtist(artist?.name)?.let { artistId ->

                    album?.text?.let { albumName ->
                        db.albumQueries.insert(
                            artistId = artistId,
                            name = albumName,
                            lowArtwork = images?.get(2)?.url,
                            highArtwork = images?.get(3)?.url
                        )
                        db.albumQueries
                            .getId(artistId, albumName)
                            .executeAsOneOrNull()
                            ?.let { albumId ->

                                name?.let { trackName ->
                                    db.trackQueries.upsertRecentTrack(
                                        artistId = artistId,
                                        albumId = albumId,
                                        name = trackName,
                                        loved = loved == 1,
                                        lovedAt = null,
                                        playCount = null,
                                        rank = null
                                    )
                                    db.trackQueries
                                        .getId(artistId, name)
                                        .executeAsOneOrNull()
                                        ?.let { trackId ->

                                            db.scrobbleQueries.upsert(
                                                trackId = trackId,
                                                listenedAt = date,
                                                nowPlaying = attributes?.nowPlaying == "true"
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