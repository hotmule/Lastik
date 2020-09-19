package ru.hotmule.lastfmclient

import com.russhwolf.settings.Settings
import ru.hotmule.lastfmclient.data.prefs.PrefsSource
import ru.hotmule.lastfmclient.data.remote.HttpClientFactory
import ru.hotmule.lastfmclient.data.remote.RemoteSource
import ru.hotmule.lastfmclient.domain.AuthInteractor

open class Sdk(
    httpClientFactory: HttpClientFactory,
    settings: Settings,
    apiKey: String,
    secret: String
) {

    private val prefs = PrefsSource(settings)
    private val remote = RemoteSource(httpClientFactory.create(prefs))

    val authInteractor = AuthInteractor(apiKey, secret, prefs, remote.authApi)

    companion object
}