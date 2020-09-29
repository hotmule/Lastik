package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.LastikDatabase

data class Attrs(
    val name: String? = null,
    val rank: Int? = null,
    val playCount: Int? = null,
    val lowResImage: String? = null,
    val highResImage: String? = null
)

data class LibraryListItem(
    val imageUrl: String? = null,
    val title: String? = null,
    val position: Int? = null,
    val subtitle: String? = null,
    val scrobbles: Int? = null,
    val time: String? = null,
    val loved: Boolean? = null,
    val onLike: ((Boolean) -> Unit)? = null
)

open class BaseInteractor(
    private val db: LastikDatabase
) {

    fun lastArtistId() = db.artistQueries.lastId().executeAsOneOrNull()
    fun lastAlbumId() = db.albumQueries.lastId().executeAsOneOrNull()
    fun lastTrackId() = db.trackQueries.lastId().executeAsOneOrNull()

    fun insertArtist(
        attrs: Attrs
    ) {
        insertAttributeAndGetId(attrs) {
            db.artistQueries.insert(it)
        }
    }

    fun insertAlbum(
        artistId: Long,
        attrs: Attrs
    ) {
        insertAttributeAndGetId(attrs) {
            db.albumQueries.insert(artistId, it)
        }
    }

    fun insertTrack(
        albumId: Long,
        attrs: Attrs,
        loved: Boolean = false,
    ) {
        insertAttributeAndGetId(attrs) {
            db.trackQueries.insert(albumId, loved, it)
        }
    }

    private fun insertAttributeAndGetId(
        attrs: Attrs,
        insertAttrParent: (Long) -> Unit
    ) {
        with(db.attributesQueries) {
            insert(attrs.name, attrs.rank, attrs.playCount, attrs.lowResImage, attrs.highResImage)
            lastId().executeAsOneOrNull()?.let {
                insertAttrParent.invoke(it)
            }
        }
    }
}