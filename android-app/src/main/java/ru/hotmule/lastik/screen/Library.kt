package ru.hotmule.lastik.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import androidx.navigation.NavController
import androidx.navigation.compose.popUpTo
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.LibrarySection
import ru.hotmule.lastik.NavGraph
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryListItem
import ru.hotmule.lastik.data.local.ListItem
import ru.hotmule.lastik.theme.barHeight


@Composable
fun LibraryList(
    sdk: Sdk,
    navController: NavController,
    currentSection: LibrarySection,
    displayWidth: Float,
    isUpdating: (Boolean) -> Unit
) {

    val isSessionActive by sdk.profileInteractor.isSessionActive.collectAsState()

    if (!isSessionActive) {
        navController.navigate(NavGraph.auth) {
            popUpTo(NavGraph.library) { inclusive = true }
        }
    }

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
        isUpdating.invoke(true)
        try {
            refreshItems.invoke(true)
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
    if (!items.isNullOrEmpty()) {
        items[0].playCount?.let {
            scrobbleWidth = displayWidth / it
        }
    }

    var moreItemsLoading by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.padding(bottom = barHeight + 16.dp)
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