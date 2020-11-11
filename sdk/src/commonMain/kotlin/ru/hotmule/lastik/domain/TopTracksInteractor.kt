package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.StatisticQueries
import ru.hotmule.lastik.data.local.TrackQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class TopTracksInteractor(
    private val api: UserApi,
    private val prefs: PrefsStore,
    private val trackQueries: TrackQueries,
    private val statisticQueries: StatisticQueries,
    private val artistsInteractor: ArtistsInteractor
) : BaseInteractor() {

    fun observeTopTracks() = trackQueries.topTracks().asFlow().mapToList().map { tracks ->
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
            currentItemsCount = trackQueries.getTopTracksCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            api.getTopTracks(page).also {
                trackQueries.transaction {

                    if (firstPage) statisticQueries.deleteSectionTop(Section.TRACKS.id)

                    it?.top?.list?.forEach { track ->

                        with(track) {
                            artistsInteractor.insertArtist(artist?.name)?.let { artistId ->

                                name?.let {

                                    trackQueries.insert(
                                        artistId = artistId,
                                        name = name,
                                        albumId = null,
                                        loved = false,
                                        lovedAt = null
                                    )

                                    trackQueries.getId(artistId, name)
                                        .executeAsOneOrNull()?.let { trackId ->
                                            statisticQueries.insert(
                                                sectionId = Section.TRACKS.id,
                                                itemId = trackId,
                                                rank = attributes?.rank,
                                                playCount = playCount,
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