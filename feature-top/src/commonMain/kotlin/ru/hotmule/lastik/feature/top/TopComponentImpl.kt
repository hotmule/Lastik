package ru.hotmule.lastik.feature.top

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
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
) : TopComponent, DIAware, ComponentContext by componentContext {

    override val shelfComponent = direct.factory<ShelfComponentParams, ShelfComponent>()(
        ShelfComponentParams(childContext("Top $index"), index)
    )

    private val store = instanceKeeper.getStore {
        TopStoreFactory(
            storeFactory = direct.instance(),
            prefsStore = direct.instance(),
            index = index
        ).create()
    }

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
        shelfComponent.onRefreshItems()
    }
}