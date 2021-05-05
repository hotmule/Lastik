package ru.hotmule.lastik

import androidx.compose.desktop.Window
import com.arkivanov.decompose.ComponentContext
import ru.hotmule.lastik.ui.compose.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import org.kodein.di.DI
import org.kodein.di.factory
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.rootComponentModule
import ru.hotmule.lastik.ui.compose.DesktopLastikTheme

fun main() {

    val di = DI { import(rootComponentModule) }
    val root by di.factory<ComponentContext, RootComponent>()

    Window("Lastik") {
        DesktopLastikTheme {
            RootContent(
                rememberRootComponent { root(it) }
            )
        }
    }
}