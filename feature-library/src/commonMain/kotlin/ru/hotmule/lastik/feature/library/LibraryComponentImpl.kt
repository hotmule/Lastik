package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.*
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import org.kodein.di.*
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.Child
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponentParams

internal class LibraryComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : LibraryComponent, DIAware, ComponentContext by componentContext {

    private val scrobbles by factory<ComponentContext, ScrobblesComponent>()
    private val profile by factory<ComponentContext, ProfileComponent>()
    private val top by factory<TopComponentParams, TopComponent>()

    override val nowPlayingComponent by instance<NowPlayingComponent>()

    private val navigation = StackNavigation<Config>()
    private val _stack = childStack(
        source = navigation,
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

    sealed class Config : Parcelable {
        @Parcelize object Scrobbles : Config()
        @Parcelize object Artists : Config()
        @Parcelize object Albums : Config()
        @Parcelize object Tracks : Config()
        @Parcelize object Profile : Config()
    }
}