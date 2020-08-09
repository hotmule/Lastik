package ru.hotmule.lastfmclient

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Image
import androidx.ui.foundation.ScrollableColumn
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.rounded.*
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp

private enum class Library(val title: String) {
    Scrobbles("Scrobbles"),
    Artists("Artists"),
    Albums("Albums"),
    Tracks("Tracks"),
    Profile("Profile")
}

@Preview
@Composable
fun LibraryScreen() {
    val currentItem = state { Library.Scrobbles }
    Scaffold(
        topBar = { TopAppBar(title = { Text(currentItem.value.title) }) },
        bodyContent = { LibraryListItem(currentItem) },
        bottomBar = { LibraryBottomNavigation(currentItem) }
    )
}

@Composable
private fun LibraryListItem(
    currentScreen: MutableState<Library>
) {
    ScrollableColumn {
        mutableListOf<String>().apply {
            for (i in 1..100)
                add("${currentScreen.value.title} $i")
        }.forEach {
            listItem(text = it)
        }
    }
}

@Composable
private fun listItem(text: String) {
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                asset = Icons.Rounded.Album,
                modifier = Modifier.gravity(align = Alignment.CenterVertically)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = text)
                Text(
                    text = text,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        Divider()
    }
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
            Icons.Rounded.Face
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
        LibraryBottomNavigationItem(
            currentScreen,
            Library.Profile,
            Icons.Rounded.Person
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