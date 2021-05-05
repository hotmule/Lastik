package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.DirectDIAware
import org.kodein.di.instance
import ru.hotmule.lastik.feature.auth.AuthComponent.*
import ru.hotmule.lastik.feature.auth.store.AuthStore.*
import ru.hotmule.lastik.feature.auth.store.AuthStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class AuthComponentImpl(
    override val directDI: DirectDI,
    private val componentContext: ComponentContext
) : AuthComponent, DirectDIAware, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        AuthStoreFactory(
            storeFactory = directDI.instance(),
            queries = directDI.instance(),
            browser = directDI.instance(),
            prefs = directDI.instance(),
            api = directDI.instance()
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
        store.accept(Intent.SignInWithLastFm)
    }
}