package ru.hotmule.lastik.feature.profile

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface ProfileComponent {

    val lovedTracksComponent: ShelfComponent

    data class Model(
        val info: Profile = Profile(),
        val friends: List<Profile> = listOf(),
        val isMoreFriendsLoading: Boolean = false,
        val isLogOutShown: Boolean = false
    )

    data class Profile(
        val username: String = "",
        val image: String = "",
        val playCount: String = "",
        val scrobblingSince: String = ""
    )

    val model: Flow<Model>

    fun onRefresh()

    fun onLoadMoreFriends()

    fun onLogOut()

    fun onLogOutConfirm()

    fun onLogOutCancel()
}
