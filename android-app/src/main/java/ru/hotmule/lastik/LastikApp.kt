package ru.hotmule.lastik

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import ru.hotmule.lastik.screen.AuthScreen
import ru.hotmule.lastik.screen.LibraryScreen
import ru.hotmule.lastik.theme.AppTheme

@Composable
fun LastikApp(
    sdk: Sdk,
    displayWidth: Float,
    backDispatcher: OnBackPressedDispatcher
) {

    val isSessionActive by sdk.profileInteractor.isSessionActive.collectAsState()

    ProvideWindowInsets {
        AppTheme {
            if (!isSessionActive)
                AuthScreen(sdk.authInteractor)
            else {
                LibraryScreen(sdk, displayWidth)
            }
        }
    }
}