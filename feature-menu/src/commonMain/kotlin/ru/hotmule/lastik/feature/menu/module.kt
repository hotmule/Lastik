package ru.hotmule.lastik.feature.menu

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory

data class MenuComponentParams(
    val componentContext: ComponentContext,
    val onSettingsOpen: () -> Unit
)

val menuComponentModule = DI.Module("menuComponent") {

    bindFactory<MenuComponentParams, MenuComponent> { params ->
        MenuComponentImpl(
            di = di,
            onSettingsOpen = params.onSettingsOpen,
            componentContext = params.componentContext
        )
    }
}