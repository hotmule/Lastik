package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.auth.AuthComponent.*
import ru.hotmule.lastik.feature.auth.store.AuthStore.*
import ru.hotmule.lastik.feature.auth.store.AuthStoreFactory
import ru.hotmule.lastik.utils.WebBrowser
import ru.hotmule.lastik.utils.getStore

class AuthComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    httpClient: LastikHttpClient,
    prefs: PrefsStore,
    private val output: (Output) -> Unit
) : AuthComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        AuthStoreFactory(
            storeFactory = storeFactory,
            authApi = httpClient.authApi,
            prefs = prefs
        ).create()
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            it.login,
            it.password,
            it.isPasswordVisible,
            it.isLoading
        )
    }

    override val events: Flow<Event> = store.labels.map {
        when (it) {
            is Label.MessageReceived -> Event.MessageReceived(it.message)
        }
    }

    override fun onLoginChanged(login: String) {
        store.accept(Intent.ChangeLogin(login))
    }

    override fun onPasswordChanged(password: String) {
        store.accept(Intent.ChangePassword(password))
    }

    override fun onPasswordVisibilityChanged() {
        store.accept(Intent.ChangePasswordVisibility)
    }

    override fun onSignIn() {
        store.accept(Intent.SignIn)
    }

    override fun onSignInWithLastFm() {
        output(Output.SignInWithLastFm)
    }
}