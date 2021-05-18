package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface ProfileComponent {

    sealed class Child {
        data class Shelf(val component: ShelfComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
    }

    data class Model(
        val profile: User = User(),
        val friends: List<User> = listOf(),
        val isMoreFriendsLoading: Boolean = false,
        val menuOpened: Boolean = false,
        val logOutConfirmationShown: Boolean = false
    )

    data class User(
        val username: String = "",
        val image: String = "",
        val playCount: String = "",
        val scrobblingSince: String = ""
    )

    val model: Flow<Model>

    val routerState: Value<RouterState<*, Child>>

    val activeChild: Value<Child>

    fun onRefresh()

    fun onLoadMoreFriends()

    fun onMenu()

    fun onOpenSettings()

    fun onLogOut()

    fun onLogOutConfirm()

    fun onLogOutCancel()

    fun onPop()
}