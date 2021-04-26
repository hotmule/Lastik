package ru.hotmule.lastik.feature.shelf

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.shelf.entity.ShelfItem

interface ShelfComponent {

    data class Model(
        val items: List<ShelfItem> = listOf(),
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false
    )

    val model: Flow<Model>
}