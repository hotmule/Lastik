package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.UserApi

class ProfileInteractor(
    private val api: UserApi,
    private val db: LastikDatabase,
) : BaseInteractor(db) {

    fun observeInfo() = db.profileQueries
        .getProfile()
        .asFlow()
        .mapToOneOrNull()

    fun observeFriends() = db.profileQueries
        .getFriends(getUserName())
        .asFlow()
        .mapToList()

    fun observeLovedTracks() = db.trackQueries.lovedTracks().asFlow().mapToList().map { tracks ->
        tracks.map {
            ListItem(
                imageUrl = it.lowArtwork,
                title = it.track,
                subtitle = it.artist,
                loved = it.loved,
                time = it.lovedAt
            )
        }
    }

    suspend fun refreshProfile(
        firstPage: Boolean
    ) {
        refreshInfo()
        refreshFriends()
        refreshLovedTracks(firstPage)
    }

    suspend fun refreshInfo(
        name: String? = getUserName()
    ) {
        api.getInfo(name).also {
            it?.user?.let { user -> insertUser(user) }
        }
    }

    private suspend fun refreshFriends() {
        api.getFriends(getUserName(), 1).also {
            db.transaction {
                db.profileQueries.deleteFriends(getUserName())
                it?.friends?.user?.forEach {
                    insertUser(
                        it,
                        getUserName()
                    )
                }
            }
        }
    }

    private suspend fun refreshLovedTracks(
        firstPage: Boolean
    ) {
        providePage(
            currentItemsCount = db.trackQueries.getLovedTracksPageCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->
            api.getLovedTracks(getUserName(), page).also {
                db.transaction {

                    if (firstPage) db.artistQueries.deleteLovedTracks(getUserName())

                    it?.loved?.list?.forEach { track ->

                        insertArtist(
                            track.artist?.name
                        )?.let { artistId ->

                            insertTrack(
                                artistId = artistId,
                                name = track.name,
                                loved = true,
                                lovedAt = track.date?.uts
                            )
                        }
                    }
                }
            }
        }
    }
}