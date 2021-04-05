package ru.hotmule.lastik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import ru.hotmule.lastik.feature.root.LastikRoot
import ru.hotmule.lastik.theme.AppTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ru.hotmule.lastik.ui.compose.LastikRoot(
                    rememberRootComponent {
                        LastikRoot(
                            componentContext = it,
                            dependencies = object : LastikRoot.Dependencies { }
                        )
                    }
                )
            }
        }

        /*
        val sdk = Sdk.create(
            this,
            BuildConfig.DEBUG,
            BuildConfig.API_KEY,
            BuildConfig.SECRET
        )

        val displayWidth = with (windowManager.defaultDisplay) {
            val size = Point()
            getSize(size)
            size.x / resources.displayMetrics.density
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = remember { SystemUiController(window) }
            CompositionLocalProvider(LocalSysUiController provides systemUiController) {
                LastikApp(sdk, displayWidth)
            }
        }
         */
    }
}