package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.LastikDatabase

data class Stat(
    val rank: Int? = null,
    val playCount: Long? = null
)

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

    fun lastArtistId() = db.artistQueries.lastId().executeAsOneOrNull()
    fun lastAlbumId() = db.albumQueries.lastId().executeAsOneOrNull()
    fun lastTrackId() = db.trackQueries.lastId().executeAsOneOrNull()

    fun insertArtist(
        name: String?,
        stat: Stat? = null
    ) {
        db.artistQueries.insert(
            getStatId(stat),
            name
        )
    }

    fun insertAlbum(
        artistId: Long,
        name: String?,
        lowArtwork: String?,
        highArtwork: String?,
        stat: Stat? = null
    ) {
        db.albumQueries.insert(
            artistId,
            getStatId(stat),
            name,
            lowArtwork,
            highArtwork
        )
    }

    fun insertTrack(
        artistId: Long,
        albumId: Long? = null,
        name: String?,
        loved: Boolean = false,
        stat: Stat? = null,
    ) {
        db.trackQueries.insert(
            artistId,
            albumId,
            getStatId(stat),
            name,
            loved
        )
    }

    private fun getStatId(
        stat: Stat?
    ) = if (stat == null) null else {
        db.statisticQueries.insert(stat.rank, stat.playCount)
        db.statisticQueries.lastId().executeAsOneOrNull()
    }
}