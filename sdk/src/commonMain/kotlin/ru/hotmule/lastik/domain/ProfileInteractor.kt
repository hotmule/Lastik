package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.local.TrackQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ProfileInteractor(
    private val api: UserApi,
    private val prefs: PrefsStore,
    private val trackQueries: TrackQueries,
    private val profileQueries: ProfileQueries,
    private val artistsInteractor: ArtistsInteractor
) : BaseInteractor() {

    val isSessionActive = prefs.isSessionActive

    fun getName() = prefs.name

    fun observeInfo() = profileQueries
        .getProfile()
        .asFlow()
        .mapToOneOrNull()

    fun observeFriends() = profileQueries
        .getFriends(prefs.name)
        .asFlow()
        .mapToList()

    fun observeLovedTracks() = trackQueries.lovedTracks().asFlow().mapToList().map { tracks ->
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
        api.getInfo().also {
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
        api.getFriends(1).also {
            profileQueries.transaction {
                profileQueries.deleteFriends(prefs.name)
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
            currentItemsCount = trackQueries.getLovedTracksPageCount().executeAsOne().toInt(),
            firstPage = firstPage
        ) { page ->
            api.getLovedTracks(page).also {
                profileQueries.transaction {

                    if (firstPage) trackQueries.dropLovedTrackDates()

                    it?.loved?.list?.forEach { track ->
                        with (track) {
                            artistsInteractor.insertArtist(artist?.name)?.let { artistId ->
                                name?.let {
                                    with(trackQueries) {
                                        upsertLovedTrack(
                                            artistId = artistId,
                                            name = name,
                                            loved = true,
                                            lovedAt = date?.uts,
                                            albumId = null
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

    fun insertUser(
        nickname: String,
        parentUser: String? = null,
        realName: String? = null,
        lowResImage: String? = null,
        highResImage: String? = null,
        playCount: Long? = null,
        registeredAt: Long? = null
    ) {
        profileQueries.upsert(
            parentUser,
            realName,
            lowResImage,
            highResImage,
            playCount,
            registeredAt,
            nickname,
            parentUser
        )
    }
}