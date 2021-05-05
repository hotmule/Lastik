package ru.hotmule.lastik.feature.scrobbles

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

val scrobblesComponentModule = DI.Module("scrobblesComponent") {

    bindFactory<ComponentContext, ScrobblesComponent> { componentContext ->
        ScrobblesComponentImpl(
            componentContext = componentContext,
            storeFactory = directDI.instance(),
            httpClient = directDI.instance(),
            database = directDI.instance(),
            prefsStore = directDI.instance()
        )
    }
}