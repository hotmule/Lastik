package ru.hotmule.lastik.data.prefs

import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString
import kotlinx.coroutines.flow.MutableStateFlow
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

class PrefsStore(private val settings: Settings) {

    companion object {
        const val TOKEN_ARG = "token"
        const val SESSION_KEY_ARG = "sessionKey"
        const val NAME_ARG = "name"
        const val TOP_ARTISTS_PERIOD_ARG = "top_artists_period"
        const val TOP_ALBUMS_PERIOD_ARG = "top_albums_period"
        const val TOP_TRACKS_PERIOD_ARG = "top_tracks_period"
    }

    var token by settings.nullableString(TOKEN_ARG)
    var name by settings.nullableString(NAME_ARG)

    val isSessionActive = MutableStateFlow(sessionKey != null)
    private val topArtistsPeriod = MutableStateFlow(topArtistsPeriodId)
    private val topAlbumsPeriod = MutableStateFlow(topAlbumsPeriodId)
    private val topTracksPeriod = MutableStateFlow(topTracksPeriodId)

    var sessionKey: String?
        get() = settings.getStringOrNull(SESSION_KEY_ARG)
        set(value) {
            isSessionActive.value = value != null
            settings.apply {
                if (value != null)
                    putString(SESSION_KEY_ARG, value)
                else
                    remove(SESSION_KEY_ARG)
            }
        }

    var topArtistsPeriodId: Int
        get() = settings.getInt(TOP_ARTISTS_PERIOD_ARG, TopPeriod.Overall.ordinal)
        set(value) {
            topArtistsPeriod.value = value
            settings.putInt(TOP_ARTISTS_PERIOD_ARG, value)
        }

    var topAlbumsPeriodId: Int
        get() = settings.getInt(TOP_ALBUMS_PERIOD_ARG, TopPeriod.Overall.ordinal)
        set(value) {
            topAlbumsPeriod.value = value
            settings.putInt(TOP_ALBUMS_PERIOD_ARG, value)
        }

    var topTracksPeriodId: Int
        get() = settings.getInt(TOP_TRACKS_PERIOD_ARG, TopPeriod.Overall.ordinal)
        set(value) {
            topTracksPeriod.value = value
            settings.putInt(TOP_TRACKS_PERIOD_ARG, value)
        }

    fun getTopPeriodId(type: TopType) = when (type) {
        TopType.Artists -> topArtistsPeriod
        TopType.Albums -> topAlbumsPeriod
        TopType.Tracks -> topTracksPeriod
    }

    fun getTopPeriod(type: TopType) = TopPeriod.values()[getTopPeriodId(type).value]

    fun clear() {
        sessionKey = null
        settings.clear()
    }
}