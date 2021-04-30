package ru.hotmule.lastik.feature.top

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface TopComponent {

    sealed class Child {
        data class Shelf(val component: ShelfComponent) : Child()
    }

    data class Model(
        val shelfIndex: Int? = null,
        val periodIndex: Int? = null,
        val periodsOpened: Boolean = false
    )

    val model: Flow<Model>

    val routerState: Value<RouterState<*, Child>>

    fun onPeriodsOpen()

    fun onPeriodsClose()

    fun onPeriodSelected(index: Int)
}