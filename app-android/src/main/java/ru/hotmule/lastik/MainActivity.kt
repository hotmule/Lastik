package ru.hotmule.lastik

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.factory
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.ui.compose.theme.LastikTheme
import ru.hotmule.lastik.ui.compose.RootContent

class MainActivity : AppCompatActivity(), DIAware {

    override val di by closestDI()
    private val root by factory<ComponentContext, RootComponent>()
    private lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        rootComponent = root(defaultComponentContext())

        setContent {
            LastikTheme {
                RootContent(
                    di = di,
                    component = rootComponent,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        rootComponent.onUrlReceived(intent.data?.toString())
    }
}