package ru.hotmule.lastik.feature.top

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

data class TopComponentParams(
    val componentContext: ComponentContext,
    val index: Int
)

val topComponentModule = DI.Module("topComponent") {

    bindFactory<TopComponentParams, TopComponent> { params ->
        TopComponentImpl(
            componentContext = params.componentContext,
            storeFactory = directDI.instance(),
            httpClient = directDI.instance(),
            database = directDI.instance(),
            prefsStore = directDI.instance(),
            index = params.index
        )
    }
}