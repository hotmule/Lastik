package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.feature.library.store.LibraryStoreFactory
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentImpl
import ru.hotmule.lastik.utils.getStore

class LibraryComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val shelves: List<(ComponentContext) -> ShelfComponent>
) : LibraryComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        prefsStore: PrefsStore
    ) : this(
        componentContext = componentContext,
        storeFactory = storeFactory,
        prefsStore = prefsStore,
        shelves = mutableListOf<(ComponentContext) -> ShelfComponent>().apply {
            for (shelfIndex in 0..4) {
                add { childContext: ComponentContext ->
                    ShelfComponentImpl(
                        componentContext = childContext,
                        storeFactory = storeFactory,
                        httpClient = httpClient,
                        prefsStore = prefsStore,
                        index = shelfIndex
                    )
                }
            }
        }
    )

    private val router = router(
        initialConfiguration = Config.Scrobbles,
        componentFactory = { configuration, componentContext ->

            val component = shelves[configuration.ordinal](componentContext)

            when (configuration) {
                Config.Scrobbles -> Child.Scrobbles(component)
                Config.Artists -> Child.Artists(component)
                Config.Albums -> Child.Albums(component)
                Config.Tracks -> Child.Tracks(component)
                Config.Profile -> Child.Profile(component)
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
        router.push(Config.values()[index])
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

    @Parcelize
    enum class Config : Parcelable {
        Scrobbles,
        Artists,
        Albums,
        Tracks,
        Profile
    }
}