package ru.hotmule.lastik

import androidx.compose.desktop.Window
import ru.hotmule.lastik.ui.compose.RootContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.hotmule.lastik.data.local.DesktopDatabaseDriver
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.prefs.DesktopPrefs
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.ui.compose.DesktopLastikTheme
import ru.hotmule.lastik.utils.DesktopBrowser

fun main() {

    Window("Lastik") {
        DesktopLastikTheme {

            val prefs = PrefsStore(DesktopPrefs())

            RootContent(
                rememberRootComponent {
                    RootComponentImpl(
                        componentContext = it,
                        storeFactory = DefaultStoreFactory,
                        httpClient = LastikHttpClient(prefs),
                        database = LastikDatabase(DesktopDatabaseDriver()),
                        prefsStore = prefs,
                        webBrowser = DesktopBrowser()
                    )
                }
            )
        }
    }
}