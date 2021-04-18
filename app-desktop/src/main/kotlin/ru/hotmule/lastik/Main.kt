package ru.hotmule.lastik

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import ru.hotmule.lastik.ui.compose.root.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.prefs.desktopPrefs
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.theme.LightColors
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.utils.DesktopBrowser

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
                        httpClient = LastikHttpClient(),
                        prefsStore = PrefsStore(desktopPrefs()),
                        webBrowser = DesktopBrowser()
                    )
                }
            )
        }
    }
}