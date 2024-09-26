package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Audiotrack
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.Image
import coil3.compose.AsyncImage
import coil3.toBitmap
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import lastik.ui_compose.generated.resources.Res
import lastik.ui_compose.generated.resources.shelves
import org.jetbrains.compose.resources.stringArrayResource
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.library.LibraryComponent

@Composable
fun LibraryContent(
    component: LibraryComponent,
) {
    val model by component.nowPlayingComponent.model.collectAsState(NowPlayingComponent.Model())

    Box {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(
                initialValue = BottomSheetValue.Collapsed,
                density = LocalDensity.current
            )
        )

        BottomSheetScaffold(
            modifier = Modifier.padding(
                bottom = 56.dp +
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
            content = {
                LibraryBody(
                    component = component,
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = if (model.isPlaying) 56.dp else 0.dp,
            sheetContent = {
                NowPlayingContent(
                    isCollapsed = bottomSheetScaffoldState.bottomSheetState.isCollapsed,
                    track = model.track,
                    artist = model.artist,
                    art = model.art
                )
            }
        )

        LibraryBottomBar(
            component = component,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LibraryBody(
    component: LibraryComponent,
) {
    Children(component.stack) {
        it.instance.let { child ->
            when (child) {
                is LibraryComponent.Child.Scrobbles -> MainContent(child.component)
                is LibraryComponent.Child.Artists -> TopContent(child.component)
                is LibraryComponent.Child.Albums -> TopContent(child.component)
                is LibraryComponent.Child.Tracks -> TopContent(child.component)
                is LibraryComponent.Child.Profile -> UserContent(child.component)
            }
        }
    }
}

@Composable
private fun NowPlayingContent(
    isCollapsed: Boolean,
    track: String,
    artist: String,
    art: Image?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .wrapContentWidth()
                .wrapContentHeight()
                .align(if (isCollapsed) Alignment.TopStart else Alignment.Center)
        ) {

            if (isCollapsed) {
                Image(
                    imageVector = Icons.Rounded.Equalizer,
                    contentDescription = "Now playing",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            if (art != null) {
                AsyncImage(
                    model = art.toBitmap(),
                    contentDescription = "artwork",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 14.dp, end = 14.dp)
                        .clip(shape = RoundedCornerShape(if (isCollapsed) 8 else 2))
                        .width(if (isCollapsed) 50.dp else 350.dp)
                        .height(if (isCollapsed) 50.dp else 350.dp)
                )
            }

            if (isCollapsed) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 14.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = track,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis
                    )

                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = artist,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryBottomBar(
    component: LibraryComponent,
    modifier: Modifier = Modifier
) {
    val activeIndex by component.activeChildIndex.subscribeAsState()
    val shelves = stringArrayResource(Res.array.shelves)
    val icons = arrayOf(
        Icons.Rounded.History,
        Icons.Rounded.Face,
        Icons.Rounded.Album,
        Icons.Rounded.Audiotrack,
        Icons.Rounded.AccountCircle
    )

    BottomNavigation(
        modifier = modifier
    ) {
        shelves.forEachIndexed { index, shelfTitle ->
            BottomNavigationItem(
                onClick = { component.onShelfSelect(index) },
                selected = index == activeIndex,
                modifier = Modifier.navigationBarsPadding(),
                label = { Text(shelfTitle) },
                icon = {
                    Icon(
                        contentDescription = shelfTitle,
                        imageVector = icons[index]
                    )
                }
            )
        }
    }
}
