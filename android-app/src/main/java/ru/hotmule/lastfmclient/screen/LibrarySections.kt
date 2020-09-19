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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.hotmule.lastfmclient.R
import ru.hotmule.lastfmclient.Sdk
import ru.hotmule.lastfmclient.components.LibraryListItem

private enum class LibrarySections(
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
            when (currentSection) {
                LibrarySections.Profile -> Profile(sdk)
                else -> {
                    LibrarySection(
                        stringResource(id = currentSection.title)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.preferredHeight(75.dp),
            ) {
                LibrarySections
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
private fun LibrarySection(title: String) {
    LazyColumnFor(
        modifier = Modifier.statusBarsPadding(),
        items = mutableListOf<String>().apply {
            for (i in 1..100) add("$title $i")
        }
    ) {
        LibraryListItem(text = it)
    }
}