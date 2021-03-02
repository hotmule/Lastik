package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.int
import com.russhwolf.settings.nullableString
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.domain.TopPeriod

class PrefsStore(private val settings: ObservableSettings) {

    companion object {
        const val SESSION_KEY_ARG = "sessionKey"
        const val TOP_ARTISTS_PERIOD_ARG = "topArtistsPeriodId"
        const val TOP_ALBUMS_PERIOD_ARG = "topAlbumsPeriodId"
        const val TOP_TRACKS_PERIOD_ARG = "topTracksPeriodId"
    }

    var name by settings.nullableString()
    var token by settings.nullableString()
    var sessionKey by settings.nullableString()

    var topArtistsPeriodId by settings.int(defaultValue = TopPeriod.Overall.ordinal)
    var topAlbumsPeriodId by settings.int(defaultValue = TopPeriod.Overall.ordinal)
    var topTracksPeriodId by settings.int(defaultValue = TopPeriod.Overall.ordinal)

    val isSessionActive = settings.getStringOrNullFlow(SESSION_KEY_ARG).map { it != null }
    val topArtistsPeriod = settings.getIntFlow(TOP_ARTISTS_PERIOD_ARG).map { TopPeriod.getById(it) }
    val topAlbumsPeriod = settings.getIntFlow(TOP_ALBUMS_PERIOD_ARG).map { TopPeriod.getById(it) }
    val topTracksPeriod = settings.getIntFlow(TOP_TRACKS_PERIOD_ARG).map { TopPeriod.getById(it) }

    fun clear() {
        settings.clear()
    }
}