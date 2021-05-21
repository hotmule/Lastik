package ru.hotmule.lastik.feature.user

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.menu.MenuComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface UserComponent {

    val lovedTracksComponent: ShelfComponent
    val menuComponent: MenuComponent

    data class Model(
        val info: User = User(),
        val friends: List<User> = listOf(),
        val isMoreFriendsLoading: Boolean = false
    )

    data class User(
        val username: String = "",
        val image: String = "",
        val playCount: String = "",
        val scrobblingSince: String = ""
    )

    val model: Flow<Model>

    fun onRefresh()

    fun onLoadMoreFriends()
}