package ru.hotmule.lastik.feature.menu

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.menu.MenuComponent.*

data class MenuComponentParams(
    val componentContext: ComponentContext,
    val output: (Output) -> Unit
)

val menuComponentModule = DI.Module("menuComponent") {

    bindFactory<MenuComponentParams, MenuComponent> { params ->
        MenuComponentImpl(
            di = di,
            output = params.output,
            componentContext = params.componentContext
        )
    }
}