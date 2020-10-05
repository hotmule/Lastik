package ru.hotmule.lastik.screen

import android.compose.utils.*
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import ru.hotmule.lastik.R
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryList
import ru.hotmule.lastik.theme.barHeight

enum class LibrarySection(
    @StringRes val title: Int,
    val icon: VectorAsset
) {
    Resents(R.string.resents, Icons.Rounded.History),
    Artists(R.string.artists, Icons.Rounded.Face),
    Albums(R.string.albums, Icons.Rounded.Album),
    Tracks(R.string.tracks, Icons.Rounded.Audiotrack),
    Profile(R.string.profile, Icons.Rounded.AccountCircle)
}

@ExperimentalLazyDsl
@Composable
fun LibraryScreen(
    sdk: Sdk,
    displayWidth: Float
) {

    val (currentSection, setCurrentSection) = savedInstanceState { LibrarySection.Resents }
    val isUpdating = mutableStateOf(false)

    Scaffold(
        topBar = {
            LibraryTopBar(
                modifier = Modifier.statusBarsHeightPlus(barHeight),
                isUpdating = isUpdating.value,
                currentSection = currentSection,
                onSignOut = sdk.signOutInteractor::signOut,
                nickname = sdk.profileInteractor.getUserName()
            )
        },
        bodyContent = {
            LibraryBody(
                modifier = Modifier.padding(bottom = it.bottom),
                sdk = sdk,
                displayWidth = displayWidth,
                currentSection = currentSection,
                isUpdating = { it1 -> isUpdating.value = it1 }
            )
        },
        bottomBar = {
            LibraryBottomBar(
                modifier = Modifier.navigationBarsHeightPlus(barHeight),
                setCurrentSection = { setCurrentSection(it) },
                currentSection = currentSection
            )
        }
    )
}

@Composable
private fun LibraryTopBar(
    modifier: Modifier = Modifier,
    currentSection: LibrarySection,
    onSignOut: () -> Unit,
    isUpdating: Boolean,
    nickname: String?,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.statusBarsPadding(),
                text = when {
                    isUpdating -> stringResource(id = R.string.updating)
                    currentSection != LibrarySection.Profile -> currentSection.name
                    else -> nickname ?: currentSection.name
                }
            )
        },
        actions = {
            when (currentSection) {
                LibrarySection.Albums -> IconButton(
                    icon = { Icon(Icons.Rounded.ViewModule) },
                    modifier = Modifier.statusBarsPadding(),
                    onClick = { },
                )
                LibrarySection.Profile -> IconButton(
                    icon = { Icon(Icons.Rounded.ExitToApp) },
                    modifier = Modifier.statusBarsPadding(),
                    onClick = { onSignOut.invoke() },
                )
                else -> {
                }
            }
        }
    )
}

@Composable
private fun LibraryBottomBar(
    modifier: Modifier = Modifier,
    setCurrentSection: (LibrarySection) -> Unit,
    currentSection: LibrarySection
) {
    BottomNavigation(
        modifier = modifier
    ) {
        LibrarySection
            .values()
            .toList()
            .forEach { section ->
                BottomNavigationItem(
                    modifier = Modifier.navigationBarsPadding(bottom = true),
                    icon = { Icon(section.icon) },
                    label = { Text(stringResource(id = section.title)) },
                    onClick = { setCurrentSection.invoke(section) },
                    selected = section == currentSection
                )
            }
    }
}

@ExperimentalLazyDsl
@Composable
private fun LibraryBody(
    modifier: Modifier = Modifier,
    sdk: Sdk,
    displayWidth: Float,
    currentSection: LibrarySection,
    isUpdating: (Boolean) -> Unit
) {
    when (currentSection) {
        LibrarySection.Resents -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                refresh = sdk.scrobblesInteractor::refreshScrobbles,
                itemsFlow = sdk.scrobblesInteractor::observeScrobbles
            )
        }
        LibrarySection.Artists -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                refresh = sdk.artistsInteractor::refreshArtists,
                itemsFlow = sdk.artistsInteractor::observeArtists
            )
        }
        LibrarySection.Albums -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                refresh = sdk.albumsInteractor::refreshAlbums,
                itemsFlow = sdk.albumsInteractor::observeAlbums
            )
        }
        LibrarySection.Tracks -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                refresh = sdk.tracksInteractor::refreshTopTracks,
                itemsFlow = sdk.tracksInteractor::observeTopTracks
            )
        }
        LibrarySection.Profile -> {
            LibraryList(
                isUpdating = isUpdating,
                refresh = sdk.tracksInteractor::refreshLovedTracks,
                itemsFlow = sdk.tracksInteractor::observeLovedTracks,
                modifier = modifier
            ) {
                ProfileHeader(
                    isUpdating = isUpdating,
                    sdk = sdk
                )
            }
        }
    }
}