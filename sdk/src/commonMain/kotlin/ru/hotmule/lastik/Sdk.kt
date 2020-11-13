package ru.hotmule.lastik

import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.HttpClientFactory
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.domain.*

open class Sdk(
    httpClientFactory: HttpClientFactory,
    sqlDriver: SqlDriver,
    settings: Settings,
    apiKey: String,
    secret: String
) {

    private val prefs = PrefsStore(settings)
    private val db = LastikDatabase(sqlDriver)
    val signOutInteractor = SignOutInteractor(prefs, db.profileQueries)
    private val httpClient = httpClientFactory.create(signOutInteractor)

    private val authApi = AuthApi(httpClient, prefs, apiKey, secret)
    private val userApi = UserApi(httpClient, prefs, apiKey)
    private val trackApi = TrackApi(httpClient, prefs, apiKey, secret)

    private val artistsInteractor = ArtistsInteractor(
        db.artistQueries
    )

    val profileInteractor = ProfileInteractor(
        userApi, prefs, db.trackQueries, db.periodQueries, db.profileQueries, artistsInteractor
    )

    val authInteractor = AuthInteractor(
        apiKey, authApi, prefs, profileInteractor
    )

    val scrobblesInteractor = ScrobblesInteractor(
        userApi, db.albumQueries, db.trackQueries, db.scrobbleQueries, artistsInteractor
    )

    val topArtistsInteractor = TopArtistsInteractor(
        userApi, db.artistQueries, db.statisticQueries, artistsInteractor
    )

    val topAlbumsInteractor = TopAlbumsInteractor(
        userApi, db.albumQueries, db.statisticQueries, artistsInteractor
    )

    val topTracksInteractor = TopTracksInteractor(
        userApi, prefs, db.trackQueries, db.statisticQueries, artistsInteractor
    )

    val trackInteractor = TrackInteractor(
        trackApi, db.trackQueries
    )

    companion object
}