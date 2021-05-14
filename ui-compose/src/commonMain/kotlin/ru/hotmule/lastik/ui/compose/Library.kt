package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryContent(
    component: LibraryComponent,
    topInset: Dp,
    bottomInset: Dp
) {
    val model by component.model.collectAsState(Model())

    Box {

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )

        BottomSheetScaffold(
            modifier = Modifier.padding(bottom = Res.Dimen.barHeight + bottomInset),
            content = {
                LibraryBody(
                    component = component,
                    topInset = topInset
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = if (model.isPlaying) BottomSheetScaffoldDefaults.SheetPeekHeight else 0.dp,
            sheetContent = {
                NowPlayingContent(
                    track = model.track,
                    artist = model.artist
                )
            }
        )

        LibraryBottomBar(
            component = component,
            bottomInset = bottomInset,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LibraryBody(
    component: LibraryComponent,
    topInset: Dp
) {
    Children(component.routerState) {
        it.instance.let { child ->
            when (child) {
                is Child.Scrobbles -> ScrobblesContent(child.component, topInset)
                is Child.Artists -> TopContent(child.component, topInset)
                is Child.Albums -> TopContent(child.component, topInset)
                is Child.Tracks -> TopContent(child.component, topInset)
                is Child.Profile -> ProfileContent(child.component, topInset)
            }
        }
    }
}

@Composable
private fun NowPlayingContent(
    track: String,
    artist: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(track)
        Text(artist)
        Text(track)
        Text(artist)
        Text(track)
        Text(artist)
        Text(track)
        Text(artist)
        Text(track)
        Text(artist)
        Text(track)
        Text(artist)
    }
}

@Composable
private fun LibraryBottomBar(
    component: LibraryComponent,
    bottomInset: Dp,
    modifier: Modifier = Modifier
) {
    val activeIndex by component.activeChildIndex.asState()

    BottomNavigation(
        modifier = modifier.height(Res.Dimen.barHeight + bottomInset)
    ) {
        Res.Array.shelves.forEachIndexed { index, shelfTitle ->
            BottomNavigationItem(
                onClick = { component.onShelfSelect(index) },
                selected = index == activeIndex,
                modifier = Modifier.padding(bottom = bottomInset),
                label = { Text(shelfTitle) },
                icon = {
                    Icon(
                        contentDescription = shelfTitle,
                        imageVector = Res.Array.shelfIcons[index]
                    )
                }
            )
        }
    }
}