package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.factory
import org.kodein.di.instance
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams
import ru.hotmule.lastik.feature.profile.ProfileComponent.Model
import ru.hotmule.lastik.feature.profile.store.ProfileStore
import ru.hotmule.lastik.feature.profile.store.ProfileStoreFactory

internal class ProfileComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
): ProfileComponent, DIAware, ComponentContext by componentContext {

    override val lovedTracksComponent = direct.factory<ShelfComponentParams, ShelfComponent>()(
        ShelfComponentParams(childContext("Profile"), 4)
    )

    private val store = instanceKeeper.getStore {
        ProfileStoreFactory(
            storeFactory = direct.instance(),
            profileQueries = direct.instance(),
            friendQueries = direct.instance(),
            prefs = direct.instance(),
            api = direct.instance(),
        ).create()
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            info = it.info,
            friends = it.friends,
            isMoreFriendsLoading = it.isMoreFriendsLoading,
            isLogOutShown = it.isLogOutShown,
        )
    }

    override fun onRefresh() {
        store.accept(ProfileStore.Intent.Refresh)
    }

    override fun onLoadMoreFriends() {
        store.accept(ProfileStore.Intent.LoadMoreFriends)
    }

    override fun onLogOut() {
        store.accept(ProfileStore.Intent.LogOut)
    }

    override fun onLogOutCancel() {
        store.accept(ProfileStore.Intent.LogOutCancel)
    }

    override fun onLogOutConfirm() {
        store.accept(ProfileStore.Intent.LogOutConfirm)
    }
}
