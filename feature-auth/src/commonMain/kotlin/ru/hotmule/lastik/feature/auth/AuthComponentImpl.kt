package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.auth.store.AuthStore.*
import ru.hotmule.lastik.feature.auth.store.AuthStoreFactory
import ru.hotmule.lastik.utils.getStore

class AuthComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    httpClient: LastikHttpClient,
    prefs: PrefsStore,
    private val output: (AuthComponent.Output) -> Unit
) : AuthComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        AuthStoreFactory(
            storeFactory = storeFactory,
            authApi = httpClient.authApi,
            output = output,
            prefs = prefs
        ).create()
    }

    override val model = store.states.map {
        AuthComponent.Model(
            it.login,
            it.password,
            it.isLoading
        )
    }

    val label = store.labels

    override fun onLoginChanged(login: String) {
        store.accept(Intent.ChangeLogin(login))
    }

    override fun onPasswordChanged(password: String) {
        store.accept(Intent.ChangePassword(password))
    }

    override fun onSignIn() {
        store.accept(Intent.SignIn)
    }

    override fun onSignInWithLastFm() {
        store.accept(Intent.SignInWithLastFm)
    }

    override fun onTokenUrlReceived(url: String) {
        store.accept(Intent.GetTokenFromUrl(url))
    }
}