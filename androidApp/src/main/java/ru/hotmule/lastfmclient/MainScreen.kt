package ru.hotmule.lastfmclient

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.rounded.Album
import androidx.ui.material.icons.rounded.Audiotrack
import androidx.ui.material.icons.rounded.History
import androidx.ui.material.icons.rounded.RecordVoiceOver
import androidx.ui.tooling.preview.Preview

private enum class Library(val title: String) {
    Scrobbles("Scrobbles"),
    Artists("Artists"),
    Albums("Albums"),
    Tracks("Tracks")
}

@Preview
@Composable
fun LibraryScreen() {

    val currentItem = state { Library.Scrobbles }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentItem.value.title) }
            )
        },
        bodyContent = {

        },
        bottomBar = {
            LibraryBottomNavigation(currentItem)
        }
    )
}

@Composable
private fun LibraryBottomNavigation(
    currentScreen: MutableState<Library>
) {
    BottomNavigation {
        LibraryBottomNavigationItem(
            currentScreen,
            Library.Scrobbles,
            Icons.Rounded.History,
        )
        LibraryBottomNavigationItem(
            currentScreen,
            Library.Artists,
            Icons.Rounded.RecordVoiceOver
        )
        LibraryBottomNavigationItem(
            currentScreen,
            Library.Albums,
            Icons.Rounded.Album
        )
        LibraryBottomNavigationItem(
            currentScreen,
            Library.Tracks,
            Icons.Rounded.Audiotrack
        )
    }
}

@Composable
private fun LibraryBottomNavigationItem(
    currentScreen: MutableState<Library>,
    libraryItem: Library,
    icon: VectorAsset
) {
    currentScreen.also {
        BottomNavigationItem(
            icon = { Icon(icon) },
            text = { Text(libraryItem.title) },
            selected = it.value == libraryItem,
            onSelected = { it.value = libraryItem }
        )
    }
}