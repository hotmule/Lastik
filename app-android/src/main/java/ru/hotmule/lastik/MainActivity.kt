package ru.hotmule.lastik

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.browser.WebBrowser
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.AndroidLastikTheme
import ru.hotmule.lastik.ui.compose.RootContent

class MainActivity : AppCompatActivity(), DIAware {

    override val di by closestDI()

    private val prefs: PrefsStore by instance()
    private val remote: LastikHttpClient by instance()
    private val browser: WebBrowser by instance()
    private val database: LastikDatabase by instance()
    private val storeFactory: StoreFactory by instance()

    lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val density = Resources.getSystem().displayMetrics.density

        setContent {
            ProvideWindowInsets {
                AndroidLastikTheme {

                    val insets = LocalWindowInsets.current.systemBars

                    rootComponent = rememberRootComponent {
                        RootComponentImpl(
                            componentContext = it,
                            storeFactory = storeFactory,
                            httpClient = remote,
                            database = database,
                            prefsStore = prefs,
                            webBrowser = browser
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