package ru.hotmule.lastik.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.auth.store.AuthStore.*

internal interface AuthStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class ChangeLogin(val login: String) : Intent()
        data class ChangePassword(val password: String) : Intent()
        object ChangePasswordVisibility : Intent()
        object SignIn : Intent()
    }

    sealed class Result {
        data class LoginChanged(val login: String) : Result()
        data class PasswordChanged(val password: String) : Result()
        object PasswordVisibilityChanged : Result()
        data class Loading(val isLoading: Boolean) : Result()
    }

    sealed class Label {
        data class MessageReceived(val message: String): Label()
    }

    data class State(
        val login: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
    )
}