package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.shelf.shelfComponentModule

val profileComponentModule = DI.Module("profileComponent") {

    importOnce(shelfComponentModule)

    bindFactory<ComponentContext, ProfileComponent> { componentContext ->
        ProfileComponentImpl(di, componentContext)
    }
}
