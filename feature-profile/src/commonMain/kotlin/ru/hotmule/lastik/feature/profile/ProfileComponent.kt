package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.profile.store.ProfileStore.*
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface ProfileComponent {

    data class Model(
        val profile: User = User(),
        val friends: List<User> = listOf(),
        val isMoreFriendsLoading: Boolean = false
    )

    sealed class Child {
        data class Shelf(val component: ShelfComponent) : Child()
    }

    val model: Flow<Model>

    val routerState: Value<RouterState<*, Child>>

    fun onRefresh()

    fun onLoadMoreFriends()

    fun onLogOut()
}