package ru.hotmule.lastfmclient

import com.russhwolf.settings.Settings
import ru.hotmule.lastfmclient.data.prefs.PrefsSource
import ru.hotmule.lastfmclient.data.remote.HttpClientFactory
import ru.hotmule.lastfmclient.data.remote.RemoteSource

open class Sdk(
    private val isDebug: Boolean,
    private val endpoint: String,
    private val httpClientFactory: HttpClientFactory,
    private val settings: Settings
) {

    private val prefs = PrefsSource(settings)
    private val remoteSource = RemoteSource(
        endpoint,
        httpClientFactory.create(isDebug, prefs)
    )

    companion object
}