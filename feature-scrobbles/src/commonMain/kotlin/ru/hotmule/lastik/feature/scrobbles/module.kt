package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.shelf.shelfComponentModule

val scrobblesComponentModule = DI.Module("scrobblesComponent") {

    importOnce(shelfComponentModule)

    bindFactory<ComponentContext, ScrobblesComponent> { componentContext ->
        ScrobblesComponentImpl(di, componentContext)
    }
}