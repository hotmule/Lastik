package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.*
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
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.feature.library.store.LibraryStoreFactory
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.ProfileComponentImpl
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentImpl
import ru.hotmule.lastik.utils.getStore

class LibraryComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val shelves: List<(ComponentContext) -> ShelfComponent>,
    private val profile: (ComponentContext) -> ProfileComponent
) : LibraryComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        database: LastikDatabase,
        prefsStore: PrefsStore
    ) : this(
        componentContext = componentContext,
        storeFactory = storeFactory,
        prefsStore = prefsStore,
        shelves = mutableListOf<(ComponentContext) -> ShelfComponent>().apply {
            for (shelfIndex in 0..3) {
                add { childContext ->
                    ShelfComponentImpl(
                        componentContext = childContext,
                        storeFactory = storeFactory,
                        httpClient = httpClient,
                        database = database,
                        prefsStore = prefsStore,
                        index = shelfIndex
                    )
                }
            }
        },
        profile = { childContext ->
            ProfileComponentImpl(
                childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                database = database,
                prefsStore = prefsStore
            )
        }
    )

    private val router = router<Config, Child>(
        initialConfiguration = Config.Scrobbles,
        componentFactory = { configuration, componentContext ->

            when (configuration) {
                is Config.Scrobbles -> Child.Scrobbles(shelves[0](componentContext))
                is Config.Artists -> Child.Artists(shelves[1](componentContext))
                is Config.Albums -> Child.Albums(shelves[2](componentContext))
                is Config.Tracks -> Child.Tracks(shelves[3](componentContext))
                is Config.Profile -> Child.Profile(profile(componentContext))
            }
        }
    )

    private val store = instanceKeeper.getStore {
        LibraryStoreFactory(
            storeFactory = storeFactory,
            prefsStore = prefsStore
        ).create()
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    override val model: Flow<Model> = store.states.map {
        Model(
            activeShelfIndex = it.activeShelfIndex,
            periodSelectable = it.periodSelectable,
            periodsOpened = it.periodsOpened,
            selectedPeriodIndex = it.selectedPeriodIndex,
            logOutAllowed = it.logOutAllowed
        )
    }

    override fun onShelfSelect(index: Int) {
        store.accept(Intent.ChangeShelf(index))
        router.push(
            when (index) {
                0 -> Config.Scrobbles
                1 -> Config.Artists
                2 -> Config.Albums
                3 -> Config.Tracks
                else -> Config.Profile
            }
        )
    }

    override fun onPeriodSelectOpen() {
        store.accept(Intent.OpenPeriods)
    }

    override fun onPeriodSelectClose() {
        store.accept(Intent.ClosePeriods)
    }

    override fun onPeriodSelected(index: Int) {
        store.accept(Intent.SavePeriod(index))
    }

    override fun onLogOut() {
        store.accept(Intent.LogOut)
    }

    sealed class Config : Parcelable {
        @Parcelize object Scrobbles : Config()
        @Parcelize object Artists : Config()
        @Parcelize object Albums : Config()
        @Parcelize object Tracks : Config()
        @Parcelize object Profile : Config()
    }
}