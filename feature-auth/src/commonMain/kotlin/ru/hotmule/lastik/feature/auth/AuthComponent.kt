package ru.hotmule.lastik.feature.auth

import kotlinx.coroutines.flow.Flow

interface AuthComponent {

    data class Model(
        val username: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false
    )

    sealed class Event {
        data class MessageReceived(val message: String?): Event()
    }

    sealed class Output {
        object SignInWithLastFm : Output()
    }

    val model: Flow<Model>

    val events: Flow<Event>

    fun onLoginChanged(login: String)

    fun onPasswordChanged(password: String)

    fun onPasswordVisibilityChanged()

    fun onSignIn()

    fun onSignInWithLastFm()
}