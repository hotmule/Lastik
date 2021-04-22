package ru.hotmule.lastik.feature.shelf

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*

class ShelfComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : ShelfComponent, ComponentContext by componentContext {

    override val model: Flow<Model> = flowOf()
}