package ru.hotmule.lastik

import android.compose.utils.Navigator
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
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
                val navigator: Navigator<Destination> = rememberSavedInstanceState(
                    saver = Navigator.saver(backDispatcher)
                ) {
                    Navigator(Destination.Library, backDispatcher)
                }
                val actions = remember(navigator) { Actions(navigator) }
                Crossfade(navigator.current) { destination ->
                    when (destination) {
                        Destination.Library -> LibraryScreen(sdk, displayWidth)
                    }
                }
            }
        }
    }
}