package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.entities.User

data class ListItem(
    val imageUrl: String? = null,
    val title: String? = null,
    val position: Int? = null,
    val subtitle: String? = null,
    val scrobbles: Long? = null,
    val time: Long? = null,
    val loved: Boolean? = null,
    val nowPlaying: Boolean? = null,
    val onLike: ((Boolean) -> Unit)? = null
)

open class BaseInteractor(
    private val db: LastikDatabase
) {

    fun getUserName() = db.profileQueries.getProfile().executeAsOneOrNull()?.userName ?: ""

    suspend fun providePage(
        currentItemsCount: Int,
        firstPage: Boolean,
        loadPage: suspend (Int) -> Unit,
    ) {
        if (firstPage || currentItemsCount.rem(50) == 0) {
            loadPage.invoke(
                if (firstPage) 1 else currentItemsCount / 50 + 1
            )
        }
    }

    fun insertArtist(
        name: String?,
        rank: Int? = null,
        playCount: Long? = null
    ): Long? {

        db.artistQueries.insert(
            getUserName(),
            name,
            rank,
            playCount
        )

        return db.artistQueries.lastId().executeAsOneOrNull()
    }

    fun insertAlbum(
        artistId: Long,
        name: String?,
        lowArtwork: String?,
        highArtwork: String?,
        rank: Int? = null,
        playCount: Long? = null
    ): Long? {

        db.albumQueries.insert(
            artistId,
            name,
            lowArtwork,
            highArtwork,
            rank,
            playCount
        )

        return db.albumQueries.lastId().executeAsOneOrNull()
    }

    fun insertTrack(
        artistId: Long,
        albumId: Long? = null,
        name: String?,
        loved: Boolean = false,
        lovedAt: Long? = null,
        rank: Int? = null,
        playCount: Long? = null
    ): Long? {

        db.trackQueries.insert(
            artistId,
            albumId,
            name,
            loved,
            lovedAt,
            rank,
            playCount
        )

        return db.trackQueries.lastId().executeAsOneOrNull()
    }

    fun insertUser(
        user: User,
        parentUser: String? = null,
    ) {
        db.profileQueries.upsert(
            parentUser,
            user.realName,
            user.image?.get(1)?.url,
            user.image?.get(2)?.url,
            user.playCount,
            user.registered?.time?.toLongOrNull(),
            user.nickname!!,
            parentUser
        )
    }
}