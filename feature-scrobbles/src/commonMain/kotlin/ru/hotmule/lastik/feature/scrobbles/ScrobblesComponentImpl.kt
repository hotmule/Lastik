package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent.*
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentImpl

class ScrobblesComponentImpl(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val shelf: (ComponentContext) -> ShelfComponent
): ScrobblesComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        database: LastikDatabase,
        prefsStore: PrefsStore,
    ): this(
        componentContext = componentContext,
        storeFactory = storeFactory,
        shelf = { childContext ->
            ShelfComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                database = database,
                prefsStore = prefsStore,
                index = 0
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

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Config : Parcelable {
        @Parcelize object Shelf : Config()
    }
}