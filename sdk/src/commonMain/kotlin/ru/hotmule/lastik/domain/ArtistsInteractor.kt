package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.ArtistQueries

class ArtistsInteractor(
    private val queries: ArtistQueries
) {

    fun insertArtist(
        name: String?
    ) = if (name != null) {
        with (queries) {
            insert(name = name)
            getId(name).executeAsOneOrNull()
        }
    } else null
}