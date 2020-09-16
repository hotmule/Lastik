package ru.hotmule.lastfmclient

import com.russhwolf.settings.Settings
import ru.hotmule.lastfmclient.data.prefs.PrefsSource
import ru.hotmule.lastfmclient.data.remote.HttpClientFactory
import ru.hotmule.lastfmclient.data.remote.RemoteSource

open class Sdk(
    httpClientFactory: HttpClientFactory,
    settings: Settings
) {

    private val prefs = PrefsSource(settings)
    private val remote = RemoteSource(httpClientFactory.create())

    companion object
}