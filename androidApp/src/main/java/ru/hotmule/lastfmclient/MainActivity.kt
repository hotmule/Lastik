package ru.hotmule.lastfmclient

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                ProvideDisplayInsets {
                    AppContent()
                }
            }
        }
    }

    @Composable
    fun AppContent() {
        if (viewModel.isSessionActive)
            LibraryScreen()
        else
            AuthScreen(viewModel)
    }
}