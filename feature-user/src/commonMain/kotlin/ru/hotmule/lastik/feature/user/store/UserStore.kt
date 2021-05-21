package ru.hotmule.lastik.feature.user.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.user.UserComponent.*
import ru.hotmule.lastik.feature.user.store.UserStore.*

internal interface UserStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object Refresh : Intent()
        object LoadMoreFriends : Intent()
    }

    sealed class Result {
        data class ProfileReceived(val profile: User) : Result()
        data class FriendsReceived(val friends: List<User>) : Result()
        data class MoreFriendsLoading(val isLoading: Boolean) : Result()
    }

    data class State(
        val info: User = User(),
        val friends: List<User> = listOf(),
        val isMoreFriendsLoading: Boolean = false
    )
}