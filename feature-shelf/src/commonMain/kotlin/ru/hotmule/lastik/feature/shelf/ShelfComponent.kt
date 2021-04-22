package ru.hotmule.lastik.feature.shelf

import kotlinx.coroutines.flow.Flow

interface ShelfComponent {

    data class Model(
        val items: List<String> = listOf()
    )

    val model: Flow<Model>
}