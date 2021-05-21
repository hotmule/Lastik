package ru.hotmule.lastik.feature.user

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.*
import ru.hotmule.lastik.feature.menu.MenuComponent
import ru.hotmule.lastik.feature.menu.MenuComponentParams
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams
import ru.hotmule.lastik.utils.getStore
import ru.hotmule.lastik.feature.user.UserComponent.Model
import ru.hotmule.lastik.feature.user.UserComponent.Output
import ru.hotmule.lastik.feature.user.store.UserStore.*
import ru.hotmule.lastik.feature.user.store.UserStoreFactory

internal class UserComponentImpl(
    override val di: DI,
    private val output: (Output) -> Unit,
    private val componentContext: ComponentContext
): UserComponent, DIAware, ComponentContext by componentContext {

    override val lovedTracksComponent = direct.factory<ShelfComponentParams, ShelfComponent>()(
        ShelfComponentParams(childContext("User"), 4)
    )

    override val menuComponent = direct.factory<MenuComponentParams, MenuComponent>()(
        MenuComponentParams(
            componentContext = childContext("Menu"),
            output = {
                when (it) {
                    MenuComponent.Output.SettingsOpened -> output(Output.SettingsOpened)
                }
            }
        )
    )

    private val store = instanceKeeper.getStore {
        UserStoreFactory(
            storeFactory = direct.instance(),
            profileQueries = direct.instance(),
            friendQueries = direct.instance(),
            api = direct.instance(),
        ).create()
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            info = it.info,
            friends = it.friends,
            isMoreFriendsLoading = it.isMoreFriendsLoading
        )
    }

    override fun onRefresh() {
        store.accept(Intent.Refresh)
    }

    override fun onLoadMoreFriends() {
        store.accept(Intent.LoadMoreFriends)
    }
}