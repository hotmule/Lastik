package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.factory
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponentParams

internal class ScrobblesComponentImpl(
    override val di: DI,
    private val onSettingsOpen: () -> Unit,
    private val componentContext: ComponentContext
): ScrobblesComponent, DIAware, ComponentContext by componentContext {

    override val shelfComponent = direct.factory<ShelfComponentParams, ShelfComponent>()(
        ShelfComponentParams(childContext("Scrobbles"), 0)
    )

    override fun onOpenSettings() {
        onSettingsOpen()
    }
}
