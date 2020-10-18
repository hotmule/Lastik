package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
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
    private val db: LastikDatabase,
    private val prefs: PrefsStore
) {

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

        val nickname = prefs.name

        if (name != null && nickname != null) {
            db.artistQueries.insert(
                nickname,
                name,
                rank,
                playCount
            )
            return db.artistQueries.lastId().executeAsOneOrNull()
        }

        return null
    }

    fun insertAlbum(
        artistId: Long,
        name: String?,
        lowArtwork: String?,
        highArtwork: String?,
        rank: Int? = null,
        playCount: Long? = null
    ): Long? {

        if (name != null) {
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

        return null
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

        if (name != null) {
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

        return null
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
        db.profileQueries.upsert(
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