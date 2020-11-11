package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.*
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.Date
import ru.hotmule.lastik.data.remote.entities.LibraryItem

class ScrobblesInteractor(
    private val api: UserApi,
    private val albumQueries: AlbumQueries,
    private val trackQueries: TrackQueries,
    private val scrobbleQueries: ScrobbleQueries,
    private val artistsInteractor: ArtistsInteractor
) : BaseInteractor() {

    fun observeScrobbles() = scrobbleQueries.scrobbleData()
        .asFlow()
        .mapToList()
        .map { scrobbles ->
            scrobbles.map {
                ListItem(
                    title = it.track,
                    loved = it.loved,
                    time = if (it.listenedAt != 0L) it.listenedAt else null,
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
            currentItemsCount = scrobbleQueries.getScrobblesCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getRecentTracks(page).also { response ->
                response?.recent?.tracks?.let { recentTracks ->

                    scrobbleQueries.transaction {
                        if (firstPage) scrobbleQueries.deleteAll()
                        recentTracks.forEach { insertRecentTrack(it) }
                    }
                }
            }
        }
    }

    private fun insertRecentTrack(track: LibraryItem) {

        with(track) {

            artistsInteractor.insertArtist(artist?.name)?.let { artistId ->

                album?.text?.let { albumName ->
                    albumQueries.insert(
                        artistId = artistId,
                        name = albumName,
                        lowArtwork = images?.get(2)?.url,
                        highArtwork = images?.get(3)?.url
                    )
                    albumQueries
                        .getId(artistId, albumName)
                        .executeAsOneOrNull()
                        ?.let { albumId ->

                            name?.let { trackName ->
                                trackQueries.upsertRecentTrack(
                                    artistId = artistId,
                                    albumId = albumId,
                                    name = trackName,
                                    loved = loved == 1,
                                    lovedAt = null
                                )
                                trackQueries
                                    .getId(artistId, name)
                                    .executeAsOneOrNull()
                                    ?.let { trackId ->

                                        if (date?.uts == null && attributes?.nowPlaying == "true")
                                            date = Date(0)

                                        date?.uts?.let { date ->
                                            scrobbleQueries.insert(
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