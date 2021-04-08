package ru.hotmule.lastik

import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.ui.compose.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent

fun main() {

    Window("Lastik") {
        MaterialTheme {
            DesktopTheme {
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
}