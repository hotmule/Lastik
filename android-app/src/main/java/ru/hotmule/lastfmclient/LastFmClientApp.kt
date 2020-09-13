package ru.hotmule.lastfmclient

import android.compose.utils.Navigator
import android.compose.utils.ProvideDisplayInsets
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import ru.hotmule.lastfmclient.screen.AuthScreen
import ru.hotmule.lastfmclient.screen.LibraryScreen
import ru.hotmule.lastfmclient.theme.AppTheme

@Composable
fun LastFmClientApp(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(
        saver = Navigator.saver(backDispatcher)
    ) {
        Navigator(Destination.Library, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }
    ProvideDisplayInsets {
        AppTheme {
            Crossfade(navigator.current) { destination ->
                when (destination) {
                    is Destination.Auth -> AuthScreen()
                    Destination.Library -> LibraryScreen(actions.toAuth)
                }
            }
        }
    }
}