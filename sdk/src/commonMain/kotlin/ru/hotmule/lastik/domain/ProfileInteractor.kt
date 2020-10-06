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
): BaseInteractor(db) {

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
                loved = it.loved
            )
        }
    }

    suspend fun refreshProfile() {
        refreshInfo()
        refreshFriends()
        refreshLovedTracks()
    }

    suspend fun refreshInfo(
        name: String? = getUserName()
    ) {
        api.getInfo(name).also {
            it?.user?.let { user -> insertUser(user) }
        }
    }

    private suspend fun refreshFriends() {
        api.getFriends(getUserName()).also {
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

    private suspend fun refreshLovedTracks() {
        api.getLovedTracks(getUserName()).also {
            db.transaction {
                db.artistQueries.deleteLovedTracks(getUserName())
                it?.loved?.list?.forEach { track ->
                    insertArtist(track.artist?.name)
                    lastArtistId()?.let { artistId ->
                        insertTrack(
                            artistId = artistId,
                            name = track.name,
                            loved = true
                        )
                    }
                }
            }
        }
    }
}