package ru.hotmule.lastik

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import ru.hotmule.lastik.ui.compose.root.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.theme.LightColors
import java.awt.Desktop
import java.net.URI

fun main() {

    Window("Lastik") {
        DesktopMaterialTheme(
            colors = LightColors
        ) {
            RootContent(
                rememberRootComponent {
                    RootComponentImpl(
                        componentContext = it,
                        storeFactory = DefaultStoreFactory,
                        webBrowser = { url ->
                            if (Desktop.isDesktopSupported() &&
                                Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
                            ) {
                                Desktop.getDesktop().browse(URI(url))
                            }
                        }
                    )
                }
            )
        }
    }
}