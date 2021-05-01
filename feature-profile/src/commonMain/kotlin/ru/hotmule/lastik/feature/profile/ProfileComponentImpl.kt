package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.feature.profile.store.ProfileStore.*
import ru.hotmule.lastik.feature.profile.store.ProfileStoreFactory
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentImpl
import ru.hotmule.lastik.utils.getStore

class ProfileComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val httpClient: LastikHttpClient,
    private val database: LastikDatabase,
    private val prefsStore: PrefsStore,
    private val shelf: (ComponentContext) -> ShelfComponent,
) : ProfileComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        database: LastikDatabase,
        prefsStore: PrefsStore,
    ): this(
        componentContext = componentContext,
        storeFactory = storeFactory,
        httpClient = httpClient,
        database = database,
        prefsStore = prefsStore,
        shelf = { childContext ->
            ShelfComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                database = database,
                prefsStore = prefsStore,
                index = 4
            )
        }
    )

    private val router = router<Config, Child>(
        initialConfiguration = Config.Shelf,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Config.Shelf -> Child.Shelf(shelf(componentContext))
            }
        }
    )

    private val store = instanceKeeper.getStore {
        ProfileStoreFactory(
            storeFactory = storeFactory,
            profileQueries = database.profileQueries,
            friendQueries = database.friendQueries,
            prefsStore = prefsStore,
            api = httpClient.userApi,
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