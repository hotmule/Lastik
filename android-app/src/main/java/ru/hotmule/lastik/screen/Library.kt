package ru.hotmule.lastik.screen

import android.compose.utils.*
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import ru.hotmule.lastik.R
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryListItem
import ru.hotmule.lastik.components.ListItem

val BarsHeight = 56.dp

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
    toProfile: () -> Unit
) {

    val (currentSection, setCurrentSection) = savedInstanceState { LibrarySection.Scrobbles }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsHeightPlus(BarsHeight),
                title = {
                    Text(
                        modifier = Modifier.statusBarsPadding(),
                        text = currentSection.name
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
        bodyContent = {
            LibrarySection(
                modifier = Modifier.padding(bottom = it.bottom),
                items = generateItems(currentSection)
            )
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.navigationBarsHeightPlus(BarsHeight)
            ) {
                LibrarySection
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
    items: List<ListItem>
) {

    ScrollableColumn(modifier = modifier) {
        items.forEach {
            LibraryListItem(item = it)
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

fun generateItems(
    currentSection: LibrarySection
) = mutableListOf<ListItem>().apply {
    for (i in 1..20) {
        add(
            when (currentSection) {
                LibrarySection.Scrobbles -> {
                    ListItem(
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        title = "Man",
                        subtitle = "Skepta",
                        time = "21 hours ago"
                    )
                }
                LibrarySection.Artists -> {
                    ListItem(
                        position = i,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/03/Skepta_photo.PNG",
                        title = "Skepta",
                        scrobbles = 500
                    )
                }
                LibrarySection.Albums -> {
                    ListItem(
                        position = i,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        title = "Konnichiva",
                        subtitle = "Skepta",
                        scrobbles = 500
                    )
                }
                LibrarySection.Tracks -> {
                    ListItem(
                        position = i,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        title = "Shutdown",
                        subtitle = "Skepta",
                        scrobbles = 500
                    )
                }
                LibrarySection.Loved -> {
                    ListItem(
                        loved = true,
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/08/Konnichiwa_by_Skepta_cover.jpg",
                        title = "Man",
                        subtitle = "Skepta",
                        scrobbles = 500
                    )
                }
            }
        )
    }
}