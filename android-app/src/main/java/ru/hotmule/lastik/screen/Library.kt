package ru.hotmule.lastik.screen

import android.compose.utils.*
import androidx.annotation.StringRes
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.R
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryListItem
import ru.hotmule.lastik.domain.ListItem
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
                nickname = sdk.profileInteractor.getName()
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
                loadItems = sdk.scrobblesInteractor::loadScrobbles,
                itemsFlow = sdk.scrobblesInteractor::observeScrobbles
            )
        }
        LibrarySection.Artists -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                loadItems = sdk.topArtistsInteractor::refreshArtists,
                itemsFlow = sdk.topArtistsInteractor::observeArtists
            )
        }
        LibrarySection.Albums -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                loadItems = sdk.topAlbumsInteractor::refreshAlbums,
                itemsFlow = sdk.topAlbumsInteractor::observeAlbums
            )
        }
        LibrarySection.Tracks -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                loadItems = sdk.topTracksInteractor::refreshTopTracks,
                itemsFlow = sdk.topTracksInteractor::observeTopTracks
            )
        }
        LibrarySection.Profile -> {
            LibraryList(
                modifier = modifier,
                isUpdating = isUpdating,
                loadItems = sdk.profileInteractor::refreshProfile,
                itemsFlow = sdk.profileInteractor::observeLovedTracks
            ) {
                ProfileHeader(
                    interactor = sdk.profileInteractor
                )
            }
        }
    }
}

@Composable
fun LibraryList(
    modifier: Modifier = Modifier,
    isUpdating: (Boolean) -> Unit,
    displayWidth: Float? = null,
    loadItems: suspend (Boolean) -> Unit,
    itemsFlow: () -> Flow<List<ListItem>>,
    header: @Composable (() -> Unit)? = null
) {

    LaunchedTask {
        isUpdating.invoke(true)
        try {
            loadItems.invoke(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isUpdating.invoke(false)
    }

    val items = itemsFlow
        .invoke()
        .collectAsState(initial = listOf())
        .value

    var scrobbleWidth: Float? = null
    if (!items.isNullOrEmpty() && displayWidth != null) {
        items[0].scrobbles?.let {
            scrobbleWidth = displayWidth / it
        }
    }

    var moreItemsLoading by mutableStateOf(false)

    LazyColumnForIndexed(
        modifier = modifier,
        items = items
    ) { index, item ->

        when (index) {
            0 -> header?.invoke()
            items.lastIndex -> {
                LaunchedTask {
                    moreItemsLoading = true
                    try {
                        loadItems.invoke(false)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    moreItemsLoading = false
                }
            }
        }

        LibraryListItem(
            item = item,
            scrobbleWidth = scrobbleWidth
        )

        if (index == items.lastIndex && (index + 1).rem(50) == 0)
            PagingProgress()
    }
}

@Composable
fun PagingProgress(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        )
    }
}