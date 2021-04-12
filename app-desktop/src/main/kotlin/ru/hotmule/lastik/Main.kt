package ru.hotmule.lastik

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.ui.compose.root.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.theme.LightColors

fun main() {

    Window("Lastik") {
        DesktopMaterialTheme(
            colors = LightColors
        ) {
            RootContent(
                rememberRootComponent {
                    RootComponentImpl(
                        componentContext = it,
                        storeFactory = DefaultStoreFactory
                    )
                }
            )
        }
    }
}