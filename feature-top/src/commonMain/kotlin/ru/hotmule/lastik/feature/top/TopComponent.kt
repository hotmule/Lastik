package ru.hotmule.lastik.feature.top

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.shelf.ShelfComponent

interface TopComponent {

    val shelfComponent: ShelfComponent

    data class Model(
        val shelfIndex: Int? = null,
        val periodIndex: Int? = null,
        val periodsOpened: Boolean = false
    )

    val model: Flow<Model>

    fun onPeriodsOpen()

    fun onPeriodsClose()

    fun onPeriodSelected(index: Int)
}