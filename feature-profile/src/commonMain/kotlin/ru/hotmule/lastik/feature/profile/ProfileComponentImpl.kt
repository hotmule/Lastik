package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.*
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.feature.profile.store.ProfileStore.*
import ru.hotmule.lastik.feature.profile.store.ProfileStoreFactory
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams
import ru.hotmule.lastik.utils.getStore

internal class ProfileComponentImpl internal constructor(
    override val di: DI,
    private val componentContext: ComponentContext
) : ProfileComponent, DIAware, ComponentContext by componentContext {

    private val shelf by factory<ShelfComponentParams, ShelfComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Shelf,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Config.Shelf -> Child.Shelf(shelf(ShelfComponentParams(componentContext, 4)))
            }
        }
    )

    private val store = instanceKeeper.getStore {
        ProfileStoreFactory(
            storeFactory = direct.instance(),
            profileQueries = direct.instance(),
            friendQueries = direct.instance(),
            prefsStore = direct.instance(),
            api = direct.instance(),
        ).create()
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    override val model: Flow<Model> = store.states.map {
        Model(
            profile = it.profile,
            friends = it.friends,
            isMoreFriendsLoading = it.isMoreFriendsLoading
        )
    }

    override fun onRefresh() {
        store.accept(Intent.RefreshProfile)
    }

    override fun onLoadMoreFriends() {
        store.accept(Intent.LoadMoreFriends)
    }

    override fun onLogOut() {
        store.accept(Intent.LogOut)
    }

    private sealed class Config : Parcelable {
        @Parcelize object Shelf : Config()
    }
}