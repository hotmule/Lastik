package ru.hotmule.lastik.feature.top

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
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams
import ru.hotmule.lastik.feature.top.TopComponent.*
import ru.hotmule.lastik.feature.top.store.TopStore
import ru.hotmule.lastik.feature.top.store.TopStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class TopComponentImpl(
    override val di: DI,
    private val index: Int,
    private val componentContext: ComponentContext
): TopComponent, DIAware, ComponentContext by componentContext {

    private val shelf by factory<ShelfComponentParams, ShelfComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Shelf,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Config.Shelf -> Child.Shelf(shelf(ShelfComponentParams(componentContext, index)))
            }
        }
    )

    private val store = instanceKeeper.getStore {
        TopStoreFactory(
            storeFactory = direct.instance(),
            prefsStore = direct.instance(),
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