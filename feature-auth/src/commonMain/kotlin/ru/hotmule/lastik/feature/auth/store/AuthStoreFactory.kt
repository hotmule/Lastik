package ru.hotmule.lastik.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.auth.store.AuthStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class AuthStoreFactory(
    private val storeFactory: StoreFactory,
    private val authApi: AuthApi,
    private val prefs: PrefsStore,
    private val output: (AuthComponent.Output) -> Unit
) {

    fun create(): AuthStore =
        object : AuthStore, Store<Intent, State, Label> by storeFactory.create(
            name = AuthStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {
            when (intent) {
                is Intent.ChangeLogin -> dispatch(Result.LoginChanged(intent.login))
                is Intent.ChangePassword -> dispatch(Result.PasswordChanged(intent.password))
                Intent.SignIn -> signIn(getState().login, getState().password)
                Intent.SignInWithLastFm -> signInWithLastFm()
                is Intent.GetTokenFromUrl -> getTokenFromUrl(intent.url)
            }
        }

        private suspend fun signIn(
            login: String,
            password: String
        ) {
            setLoading(true)
            withContext(AppCoroutineDispatcher.IO) {
                try {
                    authApi.getSession()
                } catch (e: Exception) {
                    sendError(e.message)
                }
            }
            setLoading(false)
        }

        private fun signInWithLastFm() {
            output(AuthComponent.Output.SignInWithLastFmSelected(authApi.getAuthUrl()))
        }

        private suspend fun getTokenFromUrl(url: String) {
            setLoading(true)
            withContext(AppCoroutineDispatcher.IO) {
                if (url.contains("token")) {
                    prefs.token = url.substringAfter("token=")
                    try {
                        val session = authApi.getSession()
                        prefs.sessionKey = session?.params?.key
                    } catch (e: Exception) {
                        sendError(e.message)
                    }
                } else {
                    sendError("Sign in error")
                }
            }
            setLoading(false)
        }

        private suspend fun setLoading(isLoading: Boolean) {
            withContext(AppCoroutineDispatcher.Main) {
                dispatch(Result.Loading(isLoading))
            }
        }

        private suspend fun sendError(message: String?) {
            withContext(AppCoroutineDispatcher.Main) {
                publish(Label.ErrorReceived(message ?: "Unknown"))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = when (result) {
            is Result.LoginChanged -> copy(login = result.login)
            is Result.PasswordChanged -> copy(password = result.password)
            is Result.Loading -> copy(isLoading = result.isLoading)
        }
    }
}