package ru.hotmule.lastik.feature.shelf

import kotlinx.coroutines.flow.Flow

interface ShelfComponent {

    data class Model(
        val items: List<String> = listOf(),
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false
    )

    val model: Flow<Model>
}