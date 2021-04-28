package ru.hotmule.lastik.feature.shelf

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*
import ru.hotmule.lastik.feature.shelf.store.ShelfStore.*
import ru.hotmule.lastik.feature.shelf.store.ShelfStoreFactory
import ru.hotmule.lastik.utils.getStore

class ShelfComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    httpClient: LastikHttpClient,
    database: LastikDatabase,
    prefsStore: PrefsStore,
    index: Int
) : ShelfComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ShelfStoreFactory(
            storeFactory = storeFactory,
            database = database,
            prefs = prefsStore,
            api = httpClient.userApi,
            index = index
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