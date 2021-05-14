package ru.hotmule.lastik

import android.content.*
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.factory
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.ui.compose.AndroidLastikTheme
import ru.hotmule.lastik.ui.compose.RootContent

class MainActivity : AppCompatActivity(), DIAware {

    override val di by closestDI()
    private val root by factory<ComponentContext, RootComponent>()
    private lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets {
                AndroidLastikTheme {

                    val insets = LocalWindowInsets.current.systemBars
                    val density = Resources.getSystem().displayMetrics.density

                    rootComponent = rememberRootComponent { root(it) }

                    RootContent(
                        di,
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
        rootComponent.onUrlReceived(intent?.data?.toString())
    }
}