package ru.hotmule.lastik

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.ui.compose.root.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import ru.hotmule.lastik.ui.compose.theme.LightColors

fun main() {

    Window("Lastik") {
        DesktopMaterialTheme(
            colors = LightColors
        ) {
            RootContent(
                rememberRootComponent {
                    RootComponent(
                        context = it,
                        dependencies = object : RootComponent.Dependencies {

                        }
                    )
                }
            )
        }
    }
}