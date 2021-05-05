package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

val profileComponentModule = DI.Module("profileComponent") {

    bindFactory<ComponentContext, ProfileComponent> { componentContext ->
        ProfileComponentImpl(
            componentContext = componentContext,
            storeFactory = directDI.instance(),
            httpClient = directDI.instance(),
            database = directDI.instance(),
            prefsStore = directDI.instance()
        )
    }
}