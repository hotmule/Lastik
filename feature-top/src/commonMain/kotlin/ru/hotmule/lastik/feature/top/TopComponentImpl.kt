package ru.hotmule.lastik.feature.top

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
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentImpl
import ru.hotmule.lastik.feature.top.TopComponent.*
import ru.hotmule.lastik.feature.top.store.TopStore
import ru.hotmule.lastik.feature.top.store.TopStoreFactory
import ru.hotmule.lastik.utils.getStore

class TopComponentImpl(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val index: Int,
    private val shelf: (ComponentContext) -> ShelfComponent
): TopComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        database: LastikDatabase,
        prefsStore: PrefsStore,
        index: Int,
    ): this(
        componentContext = componentContext,
        storeFactory = storeFactory,
        prefsStore = prefsStore,
        index = index,
        shelf = { childContext ->
            ShelfComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                database = database,
                prefsStore = prefsStore,
                index = index
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
        TopStoreFactory(
            storeFactory = storeFactory,
            prefsStore = prefsStore,
            index = index
        ).create()
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    override val model: Flow<Model> = store.states.map {
        Model(
            shelfIndex = index,
            periodIndex = it.periodIndex,
            periodsOpened = it.periodsOpened,
        )
    }

    override fun onPeriodsOpen() {
        store.accept(TopStore.Intent.OpenPeriods)
    }

    override fun onPeriodsClose() {
        store.accept(TopStore.Intent.ClosePeriods)
    }

    override fun onPeriodSelected(index: Int) {
        store.accept(TopStore.Intent.SavePeriod(index))
        (routerState.value.activeChild.component as Child.Shelf).component.onRefreshItems()
    }

    private sealed class Config : Parcelable {
        @Parcelize object Shelf : Config()
    }
}