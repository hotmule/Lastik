package ru.hotmule.lastik.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.auth.store.AuthStore.*

interface AuthStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class ChangeLogin(val login: String) : Intent()
        data class ChangePassword(val password: String) : Intent()
        object SignIn : Intent()
    }

    data class State(
        val login: String = "",
        val password: String = ""
    )
}