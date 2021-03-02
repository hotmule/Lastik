package ru.hotmule.lastik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Point
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import ru.hotmule.lastik.utlis.LocalSysUiController
import ru.hotmule.lastik.utlis.SystemUiController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }
}