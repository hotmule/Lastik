package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface ScrobblesComponent {

    sealed class Child {
        data class Shelf(val component: ShelfComponent) : Child()
    }

    val routerState: Value<RouterState<*, Child>>
}