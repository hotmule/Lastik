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
    val topArtistsPeriod = MutableStateFlow(TopPeriod.getById(topArtistsPeriodId))
    val topAlbumsPeriod = MutableStateFlow(TopPeriod.getById(topAlbumsPeriodId))
    val topTracksPeriod = MutableStateFlow(TopPeriod.getById(topTracksPeriodId))

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
        get() = getTopPeriodId(TOP_ARTISTS_PERIOD_ARG)
        set(value) { setTopPeriodId(TOP_ARTISTS_PERIOD_ARG, value) }

    var topAlbumsPeriodId: Int
        get() = getTopPeriodId(TOP_ALBUMS_PERIOD_ARG)
        set(value) { setTopPeriodId(TOP_ALBUMS_PERIOD_ARG, value) }

    var topTracksPeriodId: Int
        get() = getTopPeriodId(TOP_TRACKS_PERIOD_ARG)
        set(value) { setTopPeriodId(TOP_TRACKS_PERIOD_ARG, value) }

    private fun getTopPeriodId(periodArg: String) = settings.getInt(
        periodArg,
        TopPeriod.Overall.ordinal
    )

    private fun setTopPeriodId(periodArg: String, id: Int) {

        when (periodArg) {
            TOP_ARTISTS_PERIOD_ARG -> topArtistsPeriod
            TOP_ALBUMS_PERIOD_ARG -> topAlbumsPeriod
            else ->  topTracksPeriod
        }.value = TopPeriod.getById(id)

        settings.putInt(periodArg, id)
    }

    fun getTopPeriod(type: TopType) = when (type) {
        TopType.Artists -> topArtistsPeriod
        TopType.Albums -> topAlbumsPeriod
        TopType.Tracks -> topTracksPeriod
    }

    fun clear() {
        sessionKey = null
        settings.clear()
    }
}