package ru.hotmule.lastik.feature.profile.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.profile.ProfileComponent

internal interface ProfileStore : Store<ProfileStore.Intent, ProfileStore.State, Nothing> {

    sealed class Intent {
        data object Refresh : Intent()
        data object LoadMoreFriends : Intent()
        data object LogOut : Intent()
        data object LogOutCancel : Intent()
        data object LogOutConfirm : Intent()
    }

    sealed class Result {
        data class ProfileReceived(val profile: ProfileComponent.Profile) : Result()
        data class FriendsReceived(val friends: List<ProfileComponent.Profile>) : Result()
        data class MoreFriendsLoading(val isLoading: Boolean) : Result()
        data object LoggingOut : Result()
        data object LoggingOutCanceled : Result()
    }

    data class State(
        val info: ProfileComponent.Profile = ProfileComponent.Profile(),
        val friends: List<ProfileComponent.Profile> = listOf(),
        val isMoreFriendsLoading: Boolean = false,
        val isLogOutShown: Boolean = false
    )
}
