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
    private val database = LastikDatabase(sqlDriver)
    val signOutInteractor = SignOutInteractor(prefs, database.profileQueries)
    private val httpClient = httpClientFactory.create(signOutInteractor)

    private val authApi = AuthApi(httpClient, prefs, apiKey, secret)
    private val userApi = UserApi(httpClient, apiKey)
    private val trackApi = TrackApi(httpClient, prefs, apiKey, secret)

    val profileInteractor = ProfileInteractor(userApi, database, prefs)
    val authInteractor = AuthInteractor(authApi, database, prefs, apiKey)

    val scrobblesInteractor = ScrobblesInteractor(userApi, database, prefs)
    val artistsInteractor = ArtistsInteractor(userApi, database, prefs)
    val albumsInteractor = AlbumsInteractor(userApi, database, prefs)
    val tracksInteractor = TracksInteractor(userApi, database, prefs)

    companion object
}