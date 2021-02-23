package ru.hotmule.lastik

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var isUpdating by remember { mutableStateOf(false) }

    val isLibrary = navBackStackEntry
        ?.arguments
        ?.getString(KEY_ROUTE) == NavGraph.library

    val currentLibrarySectionId = navBackStackEntry
        ?.arguments
        ?.getInt(NavGraph.Args.sectionId)
        ?: LibrarySection.Resents.ordinal

    val currentLibrarySection = LibrarySection.values()[currentLibrarySectionId]

    Scaffold(
        topBar = {
            if (isLibrary) {
                LibraryTopBar(sdk, currentLibrarySection, isUpdating)
            }
        },
        bodyContent = {
            MainNavHost(navController, sdk, displayWidth) {
                isUpdating = it
            }
        },
        bottomBar = {
            if (isLibrary) {
                LibraryBottomBar(navController, currentLibrarySection)
            }
        }
    )
}

@Composable
private fun LibraryTopBar(
    sdk: Sdk,
    currentSection: LibrarySection,
    isUpdating: Boolean
) {
    TopAppBar(
        modifier = Modifier.statusBarsHeight(additional = barHeight),
        title = {
            Text(
                modifier = Modifier.statusBarsPadding(),
                text = when {
                    isUpdating -> stringResource(id = R.string.updating)
                    currentSection != LibrarySection.Profile -> currentSection.name
                    else -> sdk.profileInteractor.getName() ?: currentSection.name
                }
            )
        },
        actions = {

            when (currentSection) {

                LibrarySection.Profile -> {
                    IconButton(
                        modifier = Modifier.statusBarsPadding(),
                        onClick = { sdk.signOutInteractor.signOut() }
                    ) {
                        Icon(Icons.Rounded.ExitToApp, null)
                    }
                }

                LibrarySection.Artists, LibrarySection.Albums, LibrarySection.Tracks -> {

                    val periods = stringArrayResource(id = R.array.period)
                    val topType = TopType.values()[currentSection.ordinal - 1]

                    var expanded by remember { mutableStateOf(false) }

                    val selectedPeriodIndex = sdk.topInteractor
                        .observePeriodId(topType)
                        .collectAsState(TopPeriod.Overall.ordinal)
                        .value

                    Providers(LocalContentAlpha provides ContentAlpha.medium) {
                        Row(
                            modifier = Modifier
                                .clickable(
                                    onClick = { expanded = !expanded },
                                )
                                .statusBarsPadding()
                                .padding(end = 12.dp, top = 4.dp),
                        ) {
                            Text(
                                text = periods[selectedPeriodIndex],
                                modifier = Modifier
                                    .padding(end = 2.dp)
                            )
                            Icon(Icons.Rounded.ExpandMore, null)
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = !expanded }
                    ) {
                        Column {
                            periods.forEachIndexed { i, title ->
                                DropdownMenuItem(
                                    onClick = {
                                        sdk.topInteractor.updatePeriod(
                                            topType,
                                            TopPeriod.values()[i]
                                        )
                                        expanded = false
                                    }
                                ) {
                                    Text(text = title)
                                }
                            }
                        }
                    }
                }

                LibrarySection.Resents -> { }
            }
        }
    )
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    sdk: Sdk,
    displayWidth: Float,
    isUpdating: (Boolean) -> Unit
) {
    NavHost(navController, NavGraph.library) {
        composable(NavGraph.auth) { AuthScreen(sdk, navController) }
        composable(
            NavGraph.library, listOf(
                navArgument(NavGraph.Args.sectionId) { type = NavType.IntType })
        ) {

            val currentSectionId = it
                .arguments
                ?.getInt(NavGraph.Args.sectionId)
                ?: LibrarySection.Resents.ordinal

            LibraryList(
                sdk,
                navController,
                LibrarySection.values()[currentSectionId],
                displayWidth,
                isUpdating
            )
        }
    }
}

@Composable
private fun LibraryBottomBar(
    navController: NavController,
    currentSection: LibrarySection
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsHeight(additional = barHeight)
    ) {
        LibrarySection
            .values()
            .toList()
            .forEach { section ->

                val title = stringResource(section.title)

                BottomNavigationItem(
                    modifier = Modifier.navigationBarsPadding(bottom = true),
                    icon = { Icon(section.icon, title) },
                    label = { Text(title) },
                    selected = currentSection == section,
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