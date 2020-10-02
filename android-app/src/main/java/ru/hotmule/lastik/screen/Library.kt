package ru.hotmule.lastik.screen

import android.compose.utils.*
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.R
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryListItem
import ru.hotmule.lastik.components.ProfileImage
import ru.hotmule.lastik.domain.ListItem
import ru.hotmule.lastik.domain.ProfileInteractor
import ru.hotmule.lastik.theme.barHeight

enum class LibrarySection(
    @StringRes val title: Int,
    val icon: VectorAsset
) {
    Scrobbles(R.string.scrobbles, Icons.Rounded.History),
    Artists(R.string.artists, Icons.Rounded.Face),
    Albums(R.string.albums, Icons.Rounded.Album),
    Tracks(R.string.tracks, Icons.Rounded.Audiotrack),
    Loved(R.string.loved, Icons.Rounded.Favorite)
}

@Composable
fun LibraryScreen(
    sdk: Sdk,
    displayWidth: Float,
    toProfile: () -> Unit
) {

    val (currentSection, setCurrentSection) = savedInstanceState { LibrarySection.Scrobbles }
    val isUpdating = mutableStateOf(false)

    Scaffold(
        topBar = {
            LibraryTopBar(
                modifier = Modifier.statusBarsHeightPlus(barHeight),
                interactor = sdk.profileInteractor,
                isUpdating = isUpdating.value,
                sectionName = currentSection.name,
                toProfile = toProfile
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
    interactor: ProfileInteractor,
    isUpdating: Boolean,
    sectionName: String,
    toProfile: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.statusBarsPadding(),
                text = if (isUpdating) stringResource(id = R.string.updating) else sectionName
            )
        },
        actions = {
            val info by interactor.observeInfo().collectAsState(initial = null)
            ProfileImage(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(12.dp)
                    .width(30.dp)
                    .height(30.dp),
                url = info?.lowResImage,
                onClick = toProfile
            )
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
        LibrarySection.Scrobbles -> {
            LibraryPage(
                modifier = modifier,
                isUpdating = isUpdating,
                refresh = sdk.scrobblesInteractor::refreshScrobbles,
                itemsFlow = sdk.scrobblesInteractor::observeScrobbles
            )
        }
        LibrarySection.Artists -> {
            LibraryPage(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                refresh = sdk.artistsInteractor::refreshArtists,
                itemsFlow = sdk.artistsInteractor::observeArtists
            )
        }
        LibrarySection.Albums -> {
            LibraryPage(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                refresh = sdk.albumsInteractor::refreshAlbums,
                itemsFlow = sdk.albumsInteractor::observeAlbums
            )
        }
        LibrarySection.Tracks -> {
            LibraryPage(
                modifier = modifier,
                isUpdating = isUpdating,
                displayWidth = displayWidth,
                refresh = sdk.tracksInteractor::refreshTopTracks,
                itemsFlow = sdk.tracksInteractor::observeTopTracks
            )
        }
        LibrarySection.Loved -> {
            LibraryPage(
                modifier = modifier,
                isUpdating = isUpdating,
                refresh = sdk.tracksInteractor::refreshLovedTracks,
                itemsFlow = sdk.tracksInteractor::observeLovedTracks
            )
        }
    }
}

@Composable
private fun LibraryPage(
    modifier: Modifier = Modifier,
    isUpdating: (Boolean) -> Unit,
    displayWidth: Float? = null,
    refresh: suspend () -> Unit,
    itemsFlow: () -> Flow<List<ListItem>>,
) {

    launchInComposition {
        isUpdating.invoke(true)
        try {
            refresh.invoke()
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

    ScrollableColumn(modifier = modifier) {
        items.forEach {
            LibraryListItem(scrobbleWidth = scrobbleWidth, item = it)
        }
    }

    // LazyColumn performance is worse than ScrollableColumn
    //
    // LazyColumnFor(
    //     modifier = modifier,
    //     items = items,
    //     itemContent = { LibraryListItem(item = it) }
    // )
}