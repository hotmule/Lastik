package ru.hotmule.lastik

import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import ru.hotmule.lastik.ui.compose.RootContent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.kodein.di.DI
import org.kodein.di.factory
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.rootComponentModule
import ru.hotmule.lastik.ui.compose.theme.LastikTheme

fun main() = singleWindowApplication(
    title = "Lastik"
) {

    val di = DI { import(rootComponentModule) }
    val root by di.factory<ComponentContext, RootComponent>()

    LastikTheme {
        RootContent(
            di,
            root(DefaultComponentContext(LifecycleRegistry()))
        )
    }
}