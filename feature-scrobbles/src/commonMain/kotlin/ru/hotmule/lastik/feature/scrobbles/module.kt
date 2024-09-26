package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.shelf.shelfComponentModule

data class ScrobblesComponentParams(
    val componentContext: ComponentContext,
    val onSettingsOpen: () -> Unit
)

val scrobblesComponentModule = DI.Module("scrobblesComponent") {

    importOnce(shelfComponentModule)

    bindFactory<ScrobblesComponentParams, ScrobblesComponent> { params ->
        ScrobblesComponentImpl(di, params.onSettingsOpen, params.componentContext)
    }
}
