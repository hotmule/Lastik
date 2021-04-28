package ru.hotmule.lastik

import com.russhwolf.settings.ObservableSettings
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.HttpClientFactory
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.local.Top
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.domain.*

open class Sdk(
    httpClientFactory: HttpClientFactory,
    sqlDriver: SqlDriver,
    settings: ObservableSettings,
    apiKey: String,
    secret: String
) {

    private val prefs = PrefsStore(settings)

    private val httpClient = httpClientFactory.create()

    private val userApi = UserApi(httpClient, prefs, apiKey)
    private val trackApi = TrackApi(httpClient, prefs, apiKey, secret)

    companion object
}