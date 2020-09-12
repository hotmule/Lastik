package ru.hotmule.lastfmclient

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import androidx.ui.tooling.preview.PreviewParameter

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
    val currentItem = remember { mutableStateOf(Library.Scrobbles) }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.preferredHeight(79.dp),
                title = {
                    Text(
                        modifier = Modifier.statusBarsPadding(),
                        text = currentItem.value.title
                    )
                }
            )
        },
        bodyContent = { LibraryListItem(currentItem) },
        bottomBar = {
            LibraryBottomNavigation(currentScreen = currentItem)
        }
    )
}

@Composable
private fun LibraryListItem(
    currentScreen: MutableState<Library>
) {
    LazyColumnFor(items = mutableListOf<String>().apply {
        for (i in 1..100)
            add("${currentScreen.value.title} $i")
    }) {
        listItem(text = it)
    }
}

@Composable
private fun listItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .clickable(onClick = { })
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                asset = Icons.Rounded.Album,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier.gravity(align = Alignment.CenterVertically)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
                ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        Divider()
    }
}

@Composable
private fun LibraryBottomNavigation(
    modifier: Modifier = Modifier,
    currentScreen: MutableState<Library>
) {
    BottomNavigation(
        modifier = Modifier.preferredHeight(75.dp),
    ) {
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
            modifier = Modifier.navigationBarsPadding(bottom = true),
            icon = { Icon(icon) },
            label = { Text(libraryItem.title) },
            selected = it.value == libraryItem,
            onSelect = { it.value = libraryItem }
        )
    }
}