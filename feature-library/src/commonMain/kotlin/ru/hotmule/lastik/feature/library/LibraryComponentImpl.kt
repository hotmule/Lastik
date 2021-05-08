package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.*
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.feature.library.LibraryComponent.Child
import ru.hotmule.lastik.feature.library.store.LibraryStore.*
import ru.hotmule.lastik.feature.library.store.LibraryStoreFactory
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponentParams
import ru.hotmule.lastik.utils.getStore

internal class LibraryComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : LibraryComponent, DIAware, ComponentContext by componentContext {

    private val scrobbles by factory<ComponentContext, ScrobblesComponent>()
    private val profile by factory<ComponentContext, ProfileComponent>()
    private val top by factory<TopComponentParams, TopComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Scrobbles
    ) { configuration, componentContext ->
        when (configuration) {
            is Config.Scrobbles -> Child.Scrobbles(scrobbles(componentContext))
            is Config.Artists -> Child.Artists(top(TopComponentParams(componentContext, 1)))
            is Config.Albums -> Child.Albums(top(TopComponentParams(componentContext, 2)))
            is Config.Tracks -> Child.Tracks(top(TopComponentParams(componentContext, 3)))
            is Config.Profile -> Child.Profile(profile(componentContext))
        }
    }

    private val store = instanceKeeper.getStore {
        LibraryStoreFactory(
            storeFactory = direct.instance()
        ).create()
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            it.isPlaying,
            it.artist,
            it.track
        )
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    override val activeChildIndex: Value<Int> = routerState.map {
        it.activeChild.instance.index
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

    override fun onTrackDetected(isPlaying: Boolean?, artist: String?, track: String?, time: Long?) {
        store.accept(Intent.CheckDetectedTrack(isPlaying, artist, track))
    }

    sealed class Config : Parcelable {
        @Parcelize object Scrobbles : Config()
        @Parcelize object Artists : Config()
        @Parcelize object Albums : Config()
        @Parcelize object Tracks : Config()
        @Parcelize object Profile : Config()
    }
}