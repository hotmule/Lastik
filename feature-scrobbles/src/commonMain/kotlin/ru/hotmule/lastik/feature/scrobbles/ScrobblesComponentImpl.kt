package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import org.kodein.di.*
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams

internal class ScrobblesComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
): ScrobblesComponent, DIAware, ComponentContext by componentContext {

    override val shelfComponent = direct.factory<ShelfComponentParams, ShelfComponent>()(
        ShelfComponentParams(childContext("Scrobbles"), 0)
    )
}