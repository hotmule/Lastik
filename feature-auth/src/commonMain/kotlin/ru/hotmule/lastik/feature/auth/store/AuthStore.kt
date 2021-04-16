package ru.hotmule.lastik.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.auth.store.AuthStore.*

interface AuthStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class ChangeLogin(val login: String) : Intent()
        data class ChangePassword(val password: String) : Intent()
        object SignIn : Intent()
        object SignInWithLastFm : Intent()
        data class GetTokenFromUrl(val url: String) : Intent()
    }

    sealed class Result {
        data class LoginChanged(val login: String) : Result()
        data class PasswordChanged(val password: String) : Result()
        data class Loading(val isLoading: Boolean) : Result()
    }

    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false
    )
}