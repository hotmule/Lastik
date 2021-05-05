package ru.hotmule.lastik.feature.shelf

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory

data class ShelfComponentParams(
    val componentContext: ComponentContext,
    val index: Int
)

val shelfComponentModule = DI.Module("shelfComponent") {

    bindFactory<ShelfComponentParams, ShelfComponent> { params ->
        ShelfComponentImpl(
            componentContext = params.componentContext,
            directDI = directDI,
            index = params.index
        )
    }
}