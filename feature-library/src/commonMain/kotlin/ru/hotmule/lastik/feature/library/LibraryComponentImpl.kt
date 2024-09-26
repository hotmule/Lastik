package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.factory
import org.kodein.di.instance
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.Child
import ru.hotmule.lastik.feature.main.MainComponent
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponentParams
import ru.hotmule.lastik.feature.profile.ProfileComponent

internal class LibraryComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : LibraryComponent, DIAware, ComponentContext by componentContext {

    private val scrobbles by factory<ComponentContext, MainComponent>()
    private val profile by factory<ComponentContext, ProfileComponent>()
    private val top by factory<TopComponentParams, TopComponent>()

    override val nowPlayingComponent by instance<NowPlayingComponent>()

    private val navigation = StackNavigation<Config>()
    private val _stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
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

    override val stack: Value<ChildStack<*, Child>> = _stack

    override val activeChildIndex: Value<Int> = stack.map {
        it.active.instance.index
    }

    override fun onShelfSelect(index: Int) {
        navigation.bringToFront(
            when (index) {
                0 -> Config.Scrobbles
                1 -> Config.Artists
                2 -> Config.Albums
                3 -> Config.Tracks
                else -> Config.Profile
            }
        )
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Scrobbles : Config()
        @Serializable
        data object Artists : Config()
        @Serializable
        data object Albums : Config()
        @Serializable
        data object Tracks : Config()
        @Serializable
        data object Profile : Config()
    }
}
