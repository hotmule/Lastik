package ru.hotmule.lastik.feature.shelf

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.DirectDIAware
import org.kodein.di.instance
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*
import ru.hotmule.lastik.feature.shelf.store.ShelfStoreRepository
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*
import ru.hotmule.lastik.feature.shelf.store.ShelfStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class ShelfComponentImpl(
    override val directDI: DirectDI,
    private val index: Int,
    private val componentContext: ComponentContext,
) : ShelfComponent, DirectDIAware, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ShelfStoreFactory(
            storeFactory = instance(),
            repository = ShelfStoreRepository(
                prefsStore = instance(),
                database = instance(),
                trackApi = instance(),
                userApi = instance(),
                index = index
            )
        ).create()
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            items = it.items,
            isRefreshing = it.isRefreshing,
            isMoreLoading = it.isMoreLoading
        )
    }

    override fun onRefreshItems() {
        store.accept(Intent.RefreshItems)
    }

    override fun onLoadMoreItems() {
        store.accept(Intent.LoadMoreItems)
    }

    override fun onMakeLove(
        title: String,
        subtitle: String?,
        isLoved: Boolean
    ) {
        store.accept(Intent.MakeLove(title, subtitle, isLoved))
    }
}