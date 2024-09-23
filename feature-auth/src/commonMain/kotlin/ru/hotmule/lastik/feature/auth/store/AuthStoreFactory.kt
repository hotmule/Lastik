package ru.hotmule.lastik.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.feature.auth.store.AuthStore.*
import ru.hotmule.lastik.feature.browser.WebBrowser
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class AuthStoreFactory(
    private val storeFactory: StoreFactory,
    private val queries: ProfileQueries,
    private val browser: WebBrowser,
    private val prefs: PrefsStore,
    private val api: AuthApi
) {
    fun create(): AuthStore = object : AuthStore, Store<Intent, State, Label> by storeFactory.create(
        name = AuthStore::class.simpleName,
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Result, Label>(
        AppCoroutineDispatcher.Main
    ) {

        override fun executeAction(action: Unit) {
            scope.launch {
                prefs.tokenAsFlow.collect {
                    if (it != null) getSession(it)
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            scope.launch {
                when (intent) {
                    is Intent.ChangeLogin -> dispatch(Result.LoginChanged(intent.login))
                    is Intent.ChangePassword -> dispatch(Result.PasswordChanged(intent.password))
                    Intent.ChangePasswordVisibility -> dispatch(Result.PasswordVisibilityChanged)
                    Intent.SignIn -> signIn(state().login, state().password)
                    Intent.SignInWithLastFm -> browser.open(api.authUrl)
                }
            }
        }

        private suspend fun signIn(
            login: String,
            password: String
        ) {
            launch {
                api.getMobileSession(login, password)?.also {
                    prefs.login = login
                    prefs.password = password
                    prefs.sessionKey = it.params?.key
                    queries.insert(it.params?.name)
                }
            }
        }

        private suspend fun getSession(
            token: String
        ) {
            launch {
                api.getSession(token)?.also {
                    prefs.token = token
                    prefs.sessionKey = it.params?.key
                    queries.insert(it.params?.name)
                }
            }
        }

        private suspend fun launch(
            unit: suspend () -> Unit
        ) {
            try {
                dispatch(Result.Loading(true))
                withContext(AppCoroutineDispatcher.IO) { unit() }
            } catch (e: Exception) {
                publish(Label.MessageReceived(e.message))
            } finally {
                dispatch(Result.Loading(false))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(msg: Result): State = when (msg) {
            is Result.LoginChanged -> copy(login = msg.login)
            is Result.PasswordChanged -> copy(password = msg.password)
            Result.PasswordVisibilityChanged -> copy(isPasswordVisible = !isPasswordVisible)
            is Result.Loading -> copy(isLoading = msg.isLoading)
        }
    }
}