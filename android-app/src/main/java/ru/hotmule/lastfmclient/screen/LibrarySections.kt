package ru.hotmule.lastfmclient.screen

import android.compose.utils.navigationBarsPadding
import android.compose.utils.statusBarsPadding
import androidx.annotation.StringRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.hotmule.lastfmclient.R
import ru.hotmule.lastfmclient.Sdk
import ru.hotmule.lastfmclient.components.LibraryListItem
import ru.hotmule.lastfmclient.components.ListItem

private val BottomNavigationHeight = 75.dp

enum class LibrarySections(
    @StringRes val title: Int,
    val icon: VectorAsset
) {
    Scrobbles(R.string.scrobbles, Icons.Rounded.History),
    Artists(R.string.artists, Icons.Rounded.Face),
    Albums(R.string.albums, Icons.Rounded.Album),
    Tracks(R.string.tracks, Icons.Rounded.Audiotrack),
    Profile(R.string.profile, Icons.Rounded.Person)
}

@Composable
fun LibraryScreen(sdk: Sdk) {

    val (currentSection, setCurrentSection) = savedInstanceState { LibrarySections.Scrobbles }

    Scaffold(
        bodyContent = {

            val paddingsModifier = Modifier
                .statusBarsPadding()
                .padding(bottom = BottomNavigationHeight)

            when (currentSection) {
                LibrarySections.Profile -> {
                    Profile(
                        modifier = paddingsModifier,
                        sdk = sdk
                    )
                }
                else -> {
                    LibrarySection(
                        modifier = paddingsModifier,
                        items = generateItems(currentSection)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .preferredHeight(BottomNavigationHeight)
            ) {
                LibrarySections
                    .values()
                    .toList()
                    .forEach { section ->
                        BottomNavigationItem(
                            modifier = Modifier
                                .navigationBarsPadding(bottom = true),
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
    items: List<ListItem>
) {
    LazyColumnFor(
        modifier = modifier,
        items = items
    ) {
        LibraryListItem(item = it)
    }
}

fun generateItems(
    currentSection: LibrarySections
) = mutableListOf<ListItem>().apply {
    for (i in 1..20) {
        add(
            when (currentSection) {
                LibrarySections.Scrobbles -> {
                    ListItem(
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        liked = true,
                        title = "Man",
                        subtitle = "Skepta",
                        time = "21 hours ago"
                    )
                }
                LibrarySections.Artists -> {
                    ListItem(
                        position = i,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/03/Skepta_photo.PNG",
                        title = "Skepta",
                        scrobbles = 500
                    )
                }
                LibrarySections.Albums -> {
                    ListItem(
                        position = i,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        title = "Konnichiva",
                        subtitle = "Skepta",
                        scrobbles = 500
                    )
                }
                LibrarySections.Tracks -> {
                    ListItem(
                        position = i,
                        liked = false,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        title = "Shutdown",
                        subtitle = "Skepta",
                        scrobbles = 500
                    )
                }
                else -> ListItem(
                    position = 1,
                    liked = false,
                    imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                    title = "Shutdown",
                    subtitle = "Skepta",
                    scrobbles = 500
                )
            }
        )
    }
}