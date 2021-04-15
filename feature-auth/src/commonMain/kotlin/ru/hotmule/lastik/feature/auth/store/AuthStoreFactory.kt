package ru.hotmule.lastik.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.feature.auth.store.AuthStore.*

internal class AuthStoreFactory(
    private val storeFactory: StoreFactory,
    private val webBrowser: (String) -> Unit
) {

    fun create(): AuthStore =
        object : AuthStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = AuthStore::class.simpleName,
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Result {
        data class LoginChanged(val login: String) : Result()
        data class PasswordChanged(val password: String) : Result()
        data class Loading(val isLoading: Boolean) : Result()
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Nothing>() {

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
            withContext(Dispatchers.Main) {
                dispatch(Result.Loading(true))
                delay(1000)
                dispatch(Result.Loading(false))
            }
        }

        private fun signInWithLastFm() {
            webBrowser(
                "http://www.last.fm/api/auth/?api_key=########&cb=hotmule://lastik"
            )
        }

        private suspend fun getTokenFromUrl(url: String) {
            withContext(Dispatchers.Main) {
                dispatch(Result.Loading(true))
                delay(1000)
                dispatch(Result.Loading(false))
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