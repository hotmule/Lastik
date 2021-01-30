package ru.hotmule.lastik

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import ru.hotmule.lastik.screen.AuthScreen
import ru.hotmule.lastik.screen.LibraryList
import ru.hotmule.lastik.theme.barHeight

enum class LibrarySection(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    Resents(R.string.resents, Icons.Rounded.History),
    Artists(R.string.artists, Icons.Rounded.Face),
    Albums(R.string.albums, Icons.Rounded.Album),
    Tracks(R.string.tracks, Icons.Rounded.Audiotrack),
    Profile(R.string.profile, Icons.Rounded.AccountCircle)
}

@Composable
fun Main(
    sdk: Sdk,
    displayWidth: Float
) {

    val navController = rememberNavController()
    val isUpdating = mutableStateOf(false)

    Scaffold(
        topBar = { MainTopBar() },
        bodyContent = { MainBody(navController, sdk, it, displayWidth) },
        bottomBar = { LibraryBottomBar(navController) }
    )
}

@Composable
private fun MainTopBar() {
    TopAppBar(
        modifier = Modifier.statusBarsHeight(additional = barHeight),
        title = {
            Text(
                modifier = Modifier.statusBarsPadding(),
                text = "Test"
            )
        }
    )
}

@Composable
private fun MainBody(
    navController: NavHostController,
    sdk: Sdk,
    padding: PaddingValues,
    displayWidth: Float
) {
    NavHost(navController, NavGraph.library) {
        composable(NavGraph.auth) { AuthScreen(sdk.authInteractor, navController) }
        composable(
            NavGraph.library, listOf(
                navArgument(NavGraph.Args.sectionId) { type = NavType.IntType })
        ) {

            val currentSectionId = it
                .arguments
                ?.getInt(NavGraph.Args.sectionId)
                ?: LibrarySection.Resents.ordinal

            LibraryList(
                Modifier.padding(bottom = padding.bottom),
                sdk,
                LibrarySection.values()[currentSectionId],
                displayWidth
            )
        }
    }
}

@Composable
private fun LibraryBottomBar(
    navController: NavController
) {
    BottomNavigation {
        LibrarySection
            .values()
            .toList()
            .forEach { section ->

                val title = stringResource(section.title)
                val currentSectionId = navController
                    .currentBackStackEntry
                    ?.arguments
                    ?.getInt(NavGraph.Args.sectionId)
                    ?: LibrarySection.Resents.ordinal

                BottomNavigationItem(
                    modifier = Modifier.navigationBarsPadding(bottom = true),
                    icon = { Icon(section.icon, title) },
                    label = { Text(title) },
                    selected = false,
                    onClick = {
                        navController.navigate(
                            NavGraph.Action.toLibrary(section)
                        ) {
                            popUpTo = navController.graph.startDestination
                        }
                    }
                )
            }
    }
}

object NavGraph {

    const val auth = "auth"

    private const val libraryRoute = "library"
    const val library = "$libraryRoute/{${Args.sectionId}}"

    object Action {
        fun toLibrary(section: LibrarySection) = "$libraryRoute/${section.ordinal}"
    }

    object Args {
        const val sectionId = "sectionId"
    }
}