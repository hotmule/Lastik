package ru.hotmule.lastik.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import androidx.navigation.NavController
import androidx.navigation.compose.popUpTo
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.LibrarySection
import ru.hotmule.lastik.NavGraph
import ru.hotmule.lastik.R
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryListItem
import ru.hotmule.lastik.data.local.ListItem
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType


@Composable
private fun LibraryTopBar(
    modifier: Modifier = Modifier,
    currentSection: LibrarySection,
    onSignOut: () -> Unit,
    observeTopPeriod: (TopType) -> Flow<Int?>,
    onTopPeriodSelect: (TopType, TopPeriod) -> Unit,
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
                LibrarySection.Artists, LibrarySection.Albums, LibrarySection.Tracks -> {

                    val periods = stringArrayResource(id = R.array.period)
                    val topType = TopType.values()[currentSection.ordinal - 1]

                    var expanded by remember { mutableStateOf(false) }

                    val selectedPeriodIndex = observeTopPeriod
                        .invoke(topType)
                        .collectAsState(TopPeriod.Overall.ordinal)
                        .value ?: TopPeriod.Overall.ordinal

                    Providers(AmbientContentAlpha provides ContentAlpha.medium) {
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
                        toggle = { },
                        expanded = expanded,
                        onDismissRequest = { expanded = !expanded },
                        dropdownOffset = DpOffset(16.dp, 4.dp),
                    ) {
                        Column {
                            periods.forEachIndexed { i, title ->
                                DropdownMenuItem(
                                    onClick = {
                                        onTopPeriodSelect.invoke(
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
                LibrarySection.Profile -> IconButton(
                    modifier = Modifier.statusBarsPadding(),
                    onClick = { onSignOut.invoke() },
                ) {
                    Icon(Icons.Rounded.ExitToApp, null)
                }
                else -> { }
            }
        }
    )
}

@Composable
fun LibraryList(
    //isUpdating: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    sdk: Sdk,
    currentSection: LibrarySection,
    displayWidth: Float? = null
) {

    /*
    val isSessionActive by sdk.profileInteractor.isSessionActive.collectAsState()

    if (!isSessionActive) {
        navController.navigate(NavGraph.auth) {
            popUpTo(NavGraph.library) { inclusive = true }
        }
    }
    */

    val refreshItems: suspend (Boolean) -> Unit
    val itemsFlow: () -> Flow<List<ListItem>>

    when (currentSection) {
        LibrarySection.Resents -> {
            refreshItems = sdk.scrobblesInteractor::refreshScrobbles
            itemsFlow = sdk.scrobblesInteractor::observeScrobbles
        }
        LibrarySection.Artists -> {
            refreshItems = sdk.topInteractor::refreshArtists
            itemsFlow = sdk.topInteractor::observeArtists
        }
        LibrarySection.Albums -> {
            refreshItems = sdk.topInteractor::refreshAlbums
            itemsFlow = sdk.topInteractor::observeAlbums
        }
        LibrarySection.Tracks -> {
            refreshItems = sdk.topInteractor::refreshTopTracks
            itemsFlow = sdk.topInteractor::observeTopTracks
        }
        LibrarySection.Profile -> {
            refreshItems = sdk.profileInteractor::refreshProfile
            itemsFlow = sdk.profileInteractor::observeLovedTracks
        }
    }

    LaunchedEffect(true) {
        //isUpdating.invoke(true)
        try {
            refreshItems.invoke(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //isUpdating.invoke(false)
    }

    val items = itemsFlow
        .invoke()
        .collectAsState(initial = listOf())
        .value

    var scrobbleWidth: Float? = null
    if (!items.isNullOrEmpty() && displayWidth != null) {
        items[0].playCount?.let {
            scrobbleWidth = displayWidth / it
        }
    }

    var moreItemsLoading by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
    ) {

        if (currentSection == LibrarySection.Profile) {
            item {
                ProfileHeader(
                    interactor = sdk.profileInteractor
                )
            }
        }

        itemsIndexed(items) { index, item ->

            LibraryListItem(
                item = item,
                scrobbleWidth = scrobbleWidth,
                loveTrack = sdk.trackInteractor::setLoved
            )

            if (index == items.lastIndex) {

                LaunchedEffect(true) {
                    moreItemsLoading = true
                    try {
                        refreshItems.invoke(false)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    moreItemsLoading = false
                }

                if (moreItemsLoading) PagingProgress()
            }
        }
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