package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ProfileInteractor(
    private val api: UserApi,
    private val db: LastikDatabase,
    private val prefs: PrefsStore
) : BaseInteractor(db) {

    val isSessionActive = prefs.isSessionActive

    fun getName() = prefs.name

    fun observeInfo() = db.profileQueries
        .getProfile()
        .asFlow()
        .mapToOneOrNull()

    fun observeFriends() = db.profileQueries
        .getFriends(prefs.name)
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

    private suspend fun refreshInfo() {
        api.getInfo(prefs.name).also {
            it?.user?.let { user ->
                user.nickname?.let { name ->
                    insertUser(
                        nickname = name,
                        realName = user.realName,
                        lowResImage = user.image?.get(1)?.url,
                        highResImage = user.image?.get(2)?.url,
                        playCount = user.playCount,
                        registeredAt = user.registered?.time?.toLongOrNull()
                    )
                }
            }
        }
    }

    private suspend fun refreshFriends() {
        api.getFriends(prefs.name, 1).also {
            db.transaction {
                db.profileQueries.deleteFriends(prefs.name)
                it?.friends?.user?.forEach {
                    it.nickname?.let { name ->
                        insertUser(
                            nickname = name,
                            parentUser = prefs.name,
                            lowResImage = it.image?.get(1)?.url,
                            highResImage = it.image?.get(2)?.url
                        )
                    }
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
            api.getLovedTracks(prefs.name, page).also {
                db.transaction {

                    //if (firstPage) db.artistQueries.deleteLovedTracks(prefs.name!!)

                    it?.loved?.list?.forEach { track ->
                        with (track) {
                            insertArtist(artist?.name)?.let { artistId ->
                                name?.let {
                                    with(db.trackQueries) {
                                        upsertLovedTrack(
                                            artistId = artistId,
                                            name = name,
                                            loved = true,
                                            lovedAt = date?.uts,
                                            albumId = null,
                                            playCount = null,
                                            rank = null
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