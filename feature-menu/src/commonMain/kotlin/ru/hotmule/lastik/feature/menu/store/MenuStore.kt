package ru.hotmule.lastik.feature.menu.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.menu.store.MenuStore.*

internal interface MenuStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object ProvideMenu : Intent()
        object LogOut : Intent()
        object LogOutCancel : Intent()
        object LogOutConfirm : Intent()
    }

    sealed class Result {
        object MenuProvided: Result()
        object LoggingOut : Result()
        object LoggingOutCanceled : Result()
    }

    data class State(
        val isMenuOpened: Boolean = false,
        val isLogOutShown: Boolean = false
    )
}