package ru.hotmule.lastik

import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.HttpClientFactory
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.local.LastikDatabase
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
    private val httpClient = httpClientFactory.create(prefs)

    private val authApi = AuthApi(httpClient, apiKey)
    private val userApi = UserApi(httpClient, apiKey)

    val authInteractor = AuthInteractor(prefs, authApi, apiKey, secret)
    val profileInteractor = ProfileInteractor(prefs)

    val scrobblesInteractor = ScrobblesInteractor(prefs, userApi, database)
    val artistsInteractor = ArtistsInteractor(prefs, userApi, database)
    val albumsInteractor = AlbumsInteractor(prefs, userApi, database)

    companion object
}