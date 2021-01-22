package ru.hotmule.lastik

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import ru.hotmule.lastik.screen.AuthScreen
import ru.hotmule.lastik.screen.LibraryScreen
import ru.hotmule.lastik.theme.AppTheme

@Composable
fun LastikApp(
    sdk: Sdk,
    displayWidth: Float
) {
    ProvideWindowInsets {
        AppTheme {
            AppNavigation(sdk, displayWidth)
        }
    }
}

@Composable
fun AppNavigation(
    sdk: Sdk,
    displayWidth: Float
) {
    val navController = rememberNavController()
    NavHost(navController, "main") {
        composable("auth") { AuthScreen(sdk.authInteractor, navController) }
        composable("main") { LibraryScreen(sdk, displayWidth, navController) }
    }
}

object NavGraph {

    object Auth {
        const val methods = "methods"
        const val browser = "browser"
    }

    object Home {
        const val resents = "resents"
        const val artists = "artists"
        const val albums = "albums"
        const val profile = "profile"
    }
}