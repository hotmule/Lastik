package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.*
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.feature.profile.ProfileComponent.Child
import ru.hotmule.lastik.feature.profile.store.ProfileStore.*
import ru.hotmule.lastik.feature.profile.store.ProfileStoreFactory
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams
import ru.hotmule.lastik.utils.getStore

internal class ProfileComponentImpl internal constructor(
    override val di: DI,
    private val componentContext: ComponentContext
) : ProfileComponent, DIAware, ComponentContext by componentContext {

    private val shelf by factory<ShelfComponentParams, ShelfComponent>()
    private val settings by factory<ComponentContext, SettingsComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Shelf,
        handleBackButton = true
    ) { configuration, componentContext ->
        when (configuration) {
            is Config.Shelf -> Child.Shelf(shelf(ShelfComponentParams(componentContext, 4)))
            Config.Settings -> Child.Settings(settings(componentContext))
        }
    }

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

    override val activeChild: Value<Child> = routerState.map {
        it.activeChild.instance
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            profile = it.profile,
            friends = it.friends,
            isMoreFriendsLoading = it.isMoreFriendsLoading,
            menuOpened = it.menuOpened,
            logOutConfirmationShown = it.logOutConfirmationShown
        )
    }

    override fun onRefresh() {
        store.accept(Intent.RefreshProfile)
    }

    override fun onLoadMoreFriends() {
        store.accept(Intent.LoadMoreFriends)
    }

    override fun onMenu() {
        store.accept(Intent.ProvideMenu)
    }

    override fun onOpenSettings() {
        store.accept(Intent.ProvideMenu)
        router.push(Config.Settings)
    }

    override fun onLogOut() {
        store.accept(Intent.LogOut(isConfirmShown = true))
    }

    override fun onLogOutConfirm() {
        store.accept(Intent.LogOutConfirm)
    }

    override fun onLogOutCancel() {
        store.accept(Intent.LogOut(isConfirmShown = false))
    }

    override fun onPop() {
        router.pop()
    }

    private sealed class Config : Parcelable {
        @Parcelize object Shelf : Config()
        @Parcelize object Settings : Config()
    }
}