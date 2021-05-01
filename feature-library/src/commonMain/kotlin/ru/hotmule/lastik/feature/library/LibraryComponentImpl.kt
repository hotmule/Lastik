package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.ProfileComponentImpl
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponentImpl
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponentImpl

class LibraryComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val scrobbles: (ComponentContext) -> ScrobblesComponent,
    private val tops: List<(ComponentContext) -> TopComponent>,
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
        scrobbles = { childContext ->
            ScrobblesComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                database = database,
                prefsStore = prefsStore
            )
        },
        tops = mutableListOf<(ComponentContext) -> TopComponent>().apply {
            for (shelfIndex in 1..3) {
                add { childContext ->
                    TopComponentImpl(
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
                is Config.Scrobbles -> Child.Scrobbles(scrobbles(componentContext))
                is Config.Artists -> Child.Artists(tops[0](componentContext))
                is Config.Albums -> Child.Albums(tops[1](componentContext))
                is Config.Tracks -> Child.Tracks(tops[2](componentContext))
                is Config.Profile -> Child.Profile(profile(componentContext))
            }
        }
    )

    override val routerState: Value<RouterState<*, Child>> = router.state

    override val activeChildIndex: Value<Int> = routerState.map {
        it.activeChild.component.index
    }

    override fun onShelfSelect(index: Int) {
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

    sealed class Config : Parcelable {
        @Parcelize object Scrobbles : Config()
        @Parcelize object Artists : Config()
        @Parcelize object Albums : Config()
        @Parcelize object Tracks : Config()
        @Parcelize object Profile : Config()
    }
}