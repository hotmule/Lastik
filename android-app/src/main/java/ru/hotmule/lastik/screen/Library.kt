package ru.hotmule.lastik.screen

import android.compose.utils.*
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.R
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryListItem
import ru.hotmule.lastik.domain.ListItem

val BarsHeight = 56.dp

enum class Section(
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

    val (currentSection, setCurrentSection) = savedInstanceState { Section.Scrobbles }
    var isUpdating by mutableStateOf(false)

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsHeightPlus(BarsHeight),
                title = {
                    Text(
                        modifier = Modifier.statusBarsPadding(),
                        text = if (isUpdating)
                            stringResource(id = R.string.updating)
                        else
                            currentSection.name
                    )
                },
                actions = {
                    CoilImage(
                        data = "https://avatars2.githubusercontent.com/u/37577810?s=60&v=4",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(12.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clip(CircleShape)
                            .clickable(onClick = { toProfile() })
                    )
                }
            )
        },
        bodyContent = { padding ->

            when (currentSection) {
                Section.Scrobbles -> {
                    LibrarySection(
                        modifier = Modifier.padding(bottom = padding.bottom),
                        refresh = sdk.scrobblesInteractor::refreshScrobbles,
                        itemsFlow = sdk.scrobblesInteractor::observeScrobbles,
                        isUpdating = { isUpdating = it }
                    )
                }
                Section.Artists -> {
                    LibrarySection(
                        displayWidth = displayWidth,
                        modifier = Modifier.padding(bottom = padding.bottom),
                        refresh = sdk.artistsInteractor::refreshArtists,
                        itemsFlow = sdk.artistsInteractor::observeArtists,
                        isUpdating = { isUpdating = it }
                    )
                }
                Section.Albums -> {
                    LibrarySection(
                        displayWidth = displayWidth,
                        modifier = Modifier.padding(bottom = padding.bottom),
                        refresh = sdk.albumsInteractor::refreshAlbums,
                        itemsFlow = sdk.albumsInteractor::observeAlbums,
                        isUpdating = { isUpdating = it }
                    )
                }
                Section.Tracks -> {
                    LibrarySection(
                        displayWidth = displayWidth,
                        modifier = Modifier.padding(bottom = padding.bottom),
                        refresh = sdk.tracksInteractor::refreshTopTracks,
                        itemsFlow = sdk.tracksInteractor::observeTopTracks,
                        isUpdating = { isUpdating = it }
                    )
                }
                Section.Loved -> {
                    LibrarySection(
                        modifier = Modifier.padding(bottom = padding.bottom),
                        refresh = sdk.tracksInteractor::refreshLovedTracks,
                        itemsFlow = sdk.tracksInteractor::observeLovedTracks,
                        isUpdating = { isUpdating = it }
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.navigationBarsHeightPlus(BarsHeight)
            ) {
                Section
                    .values()
                    .toList()
                    .forEach { section ->
                        BottomNavigationItem(
                            modifier = Modifier.navigationBarsPadding(bottom = true),
                            icon = { Icon(section.icon) },
                            label = { Text(stringResource(id = section.title)) },
                            onClick = { setCurrentSection(section) },
                            selected = section == currentSection
                        )
                    }
            }
        }
    )
}

@Composable
private fun LibrarySection(
    modifier: Modifier = Modifier,
    refresh: suspend () -> Unit,
    displayWidth: Float? = null,
    itemsFlow: () -> Flow<List<ListItem>>,
    isUpdating: (Boolean) -> Unit
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