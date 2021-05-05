package ru.hotmule.lastik.feature.profile.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.feature.profile.store.ProfileStore.*

internal interface ProfileStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object LogOut : Intent()
        object RefreshProfile : Intent()
        object LoadMoreFriends : Intent()
    }

    sealed class Result {
        data class ProfileReceived(val profile: User) : Result()
        data class FriendsReceived(val friends: List<User>) : Result()
        data class MoreFriendsLoading(val isLoading: Boolean) : Result()
    }

    data class State(
        val profile: User = User(),
        val friends: List<User> = listOf(),
        val isMoreFriendsLoading: Boolean = false
    )
}