package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.feature.library.store.LibraryStore
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.feature.library.store.LibraryStoreFactory
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentImpl
import ru.hotmule.lastik.utils.getStore

class LibraryComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val scrobbles: (ComponentContext) -> ShelfComponent,
    private val artists: (ComponentContext) -> ShelfComponent,
    private val albums: (ComponentContext) -> ShelfComponent,
    private val tracks: (ComponentContext) -> ShelfComponent,
    private val profile: (ComponentContext) -> ShelfComponent,
) : LibraryComponent, ComponentContext by componentContext {

    companion object {
        private fun createShelf(
            storeFactory: StoreFactory
        ) = { childContext: ComponentContext ->
            ShelfComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory
            )
        }
    }

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        prefsStore: PrefsStore
    ) : this(
        componentContext = componentContext,
        storeFactory = storeFactory,
        prefsStore = prefsStore,
        scrobbles = createShelf(storeFactory),
        artists = createShelf(storeFactory),
        albums = createShelf(storeFactory),
        tracks = createShelf(storeFactory),
        profile = createShelf(storeFactory)
    )

    private val router = router<Config, Child>(
        initialConfiguration = Config.Scrobbles,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                Config.Scrobbles -> Child.Scrobbles(scrobbles(componentContext))
                Config.Artists -> Child.Artists(artists(componentContext))
                Config.Albums -> Child.Albums(albums(componentContext))
                Config.Tracks -> Child.Tracks(tracks(componentContext))
                Config.Profile -> Child.Profile(profile(componentContext))
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
        router.replaceCurrent(
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

    private sealed class Config : Parcelable {
        @Parcelize object Scrobbles : Config()
        @Parcelize object Artists : Config()
        @Parcelize object Albums : Config()
        @Parcelize object Tracks : Config()
        @Parcelize object Profile : Config()
    }

    /*
    @Parcelize
    enum class Config : Parcelable {
        Scrobbles,
        Artists,
        Albums,
        Tracks,
        Profile
    }
    */
}