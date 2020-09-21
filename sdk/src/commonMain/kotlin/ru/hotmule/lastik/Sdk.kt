package ru.hotmule.lastik

import com.russhwolf.settings.Settings
import ru.hotmule.lastik.data.settings.PrefsStore
import ru.hotmule.lastik.data.remote.HttpClientFactory
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.domain.AuthInteractor
import ru.hotmule.lastik.domain.ScrobblesInteractor
import ru.hotmule.lastik.domain.ProfileInteractor

open class Sdk(
    httpClientFactory: HttpClientFactory,
    settings: Settings,
    apiKey: String,
    secret: String
) {

    private val prefs = PrefsStore(settings)
    private val client = httpClientFactory.create(prefs)

    private val authApi = AuthApi(client, apiKey)
    private val userApi = UserApi(client, apiKey)

    val authInteractor = AuthInteractor(prefs, authApi, apiKey, secret)
    val scrobblesInteractor = ScrobblesInteractor(prefs, userApi)
    val profileInteractor = ProfileInteractor(prefs)

    companion object
}