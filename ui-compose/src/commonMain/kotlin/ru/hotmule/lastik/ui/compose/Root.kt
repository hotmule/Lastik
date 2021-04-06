package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.root.RootComponent

@Composable
fun RootContent(component: RootComponent) {
    Children(component.routerState) { child, _ ->
        when (child) {
            is RootComponent.Child.Main -> MainContent(child.component)
            is RootComponent.Child.Auth -> AuthContent(child.component)
        }
    }
}