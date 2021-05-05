package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.factory
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent.*
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams

internal class ScrobblesComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
): ScrobblesComponent, DIAware, ComponentContext by componentContext {

    private val shelf by factory<ShelfComponentParams, ShelfComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Shelf,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Config.Shelf -> Child.Shelf(shelf(ShelfComponentParams(componentContext, 0)))
            }
        }
    )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Config : Parcelable {
        @Parcelize object Shelf : Config()
    }
}