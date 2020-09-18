package ru.hotmule.lastfmclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import android.compose.utils.SysUiController
import android.compose.utils.SystemUiController
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    private val sdk = Sdk.create(
        this,
        BuildConfig.DEBUG,
        BuildConfig.API_KEY,
        BuildConfig.SECRET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = remember { SystemUiController(window) }
            Providers(SysUiController provides systemUiController) {
                LastFmClientApp(sdk, onBackPressedDispatcher)
            }
        }
    }
}