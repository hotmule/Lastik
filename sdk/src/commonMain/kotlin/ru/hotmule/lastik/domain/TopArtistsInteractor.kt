package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.ArtistQueries
import ru.hotmule.lastik.data.local.PeriodQueries
import ru.hotmule.lastik.data.local.StatisticQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.Period

class TopArtistsInteractor(
    private val api: UserApi,
    private val prefs: PrefsStore,
    private val artistQueries: ArtistQueries,
    private val periodQueries: PeriodQueries,
    private val statisticQueries: StatisticQueries,
    private val artistsInteractor: ArtistsInteractor
) : BaseInteractor() {

    fun observeArtists() = artistQueries.artistTop()
        .asFlow()
        .mapToList()
        .map { artists ->
            artists.map {
                ListItem(
                    title = it.name,
                    position = it.rank,
                    imageUrl = it.lowArtwork,
                    scrobbles = it.playCount,
                )
            }
        }

    suspend fun refreshArtists(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = artistQueries.getTopArtistsCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->

            val periodId = periodQueries.getPeriodLength(prefs.name!!, 1).executeAsOneOrNull()
            val period = if (periodId != null)
                Period.values()[periodId]
            else
                Period.Overall

            api.getTopArtists(page, period.value).also {
                artistQueries.transaction {

                    if (firstPage) statisticQueries.deleteSectionTop(Section.ARTISTS.id)

                    it?.top?.artists?.forEach { artist ->
                        artistsInteractor
                            .insertArtist(artist.name)?.let { artistId ->
                                statisticQueries.insert(
                                    sectionId = Section.ARTISTS.id,
                                    itemId = artistId,
                                    rank = artist.attributes?.rank,
                                    playCount = artist.playCount,
                                )
                        }
                    }
                }
            }
        }
    }
}