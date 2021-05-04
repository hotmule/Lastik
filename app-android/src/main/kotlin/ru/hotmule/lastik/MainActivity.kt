package ru.hotmule.lastik

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import ru.hotmule.lastik.data.local.DriverFactory
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.AndroidPrefs
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.AndroidLastikTheme
import ru.hotmule.lastik.ui.compose.RootContent
import ru.hotmule.lastik.utils.AndroidBrowser

class MainActivity : AppCompatActivity() {

    lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val density = Resources.getSystem().displayMetrics.density

        setContent {
            ProvideWindowInsets {
                AndroidLastikTheme {

                    val prefs = PrefsStore(AndroidPrefs(this))
                    val insets = LocalWindowInsets.current.systemBars

                    rootComponent = rememberRootComponent {
                        RootComponentImpl(
                            componentContext = it,
                            storeFactory = DefaultStoreFactory,
                            httpClient = LastikHttpClient(prefs),
                            database = LastikDatabase(DriverFactory(this).create()),
                            prefsStore = prefs,
                            webBrowser = AndroidBrowser(this)
                        )
                    }

                    RootContent(
                        rootComponent,
                        (insets.top / density).dp,
                        (insets.bottom / density).dp
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let {
            rootComponent.onTokenUrlReceived(it.toString())
        }
    }
}