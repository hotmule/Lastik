package ru.hotmule.lastik.feature.auth

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.auth.store.AuthStore

interface AuthComponent {

    val model: Flow<Model>

    fun onLoginChanged(login: String)

    fun onPasswordChanged(password: String)

    fun onSignIn()

    fun onSignInWithLastFm()

    fun onTokenUrlReceived(url: String)

    data class Model(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false
    )
}