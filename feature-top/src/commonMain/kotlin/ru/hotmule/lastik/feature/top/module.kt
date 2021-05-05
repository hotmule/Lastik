package ru.hotmule.lastik.feature.top

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.shelf.shelfComponentModule

data class TopComponentParams(
    val componentContext: ComponentContext,
    val index: Int
)

val topComponentModule = DI.Module("topComponent") {

    importOnce(shelfComponentModule)

    bindFactory<TopComponentParams, TopComponent> { params ->
        TopComponentImpl(di,params.index, params.componentContext)
    }
}