package ru.hotmule.lastik.feature.user

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.menu.menuComponentModule
import ru.hotmule.lastik.feature.shelf.shelfComponentModule

data class UserComponentParams(
    val componentContext: ComponentContext,
    val onSettingsOpen: () -> Unit
)

val userComponentModule = DI.Module("userComponent") {

    import(menuComponentModule)
    importOnce(shelfComponentModule)

    bindFactory<UserComponentParams, UserComponent> { params ->
        UserComponentImpl(di, params.onSettingsOpen, params.componentContext)
    }
}