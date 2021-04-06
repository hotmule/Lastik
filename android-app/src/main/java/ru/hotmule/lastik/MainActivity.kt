package ru.hotmule.lastik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.theme.AppTheme
import ru.hotmule.lastik.ui.compose.RootContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                RootContent(
                    rememberRootComponent {
                        RootComponent(
                            context = it,
                            dependencies = object : RootComponent.Dependencies { }
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