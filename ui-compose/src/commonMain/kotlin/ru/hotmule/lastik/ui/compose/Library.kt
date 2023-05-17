package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.hotmule.lastik.feature.app.NowPlayingComponent.*
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.ui.compose.res.Res
import ru.hotmule.lastik.ui.compose.utils.asComposeBitmap
import ru.hotmule.lastik.ui.compose.utils.navigationBarHeight
import ru.hotmule.lastik.ui.compose.utils.navigationBarPadding
import ru.hotmule.lastik.utils.Bitmap

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryContent(
    component: LibraryComponent,
) {
    val model by component.nowPlayingComponent.model.collectAsState(Model())

    Box {

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )

        BottomSheetScaffold(
            modifier = Modifier.padding(bottom = Res.Dimen.barHeight + WindowInsets.navigationBarHeight),
            content = {
                LibraryBody(
                    component = component,
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = if (model.isPlaying) Res.Dimen.shelfItemHeight else 0.dp,
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
                is Child.Scrobbles -> ScrobblesContent(child.component)
                is Child.Artists -> TopContent(child.component)
                is Child.Albums -> TopContent(child.component)
                is Child.Tracks -> TopContent(child.component)
                is Child.Profile -> ProfileContent(child.component)
            }
        }
    }
}

@Composable
private fun NowPlayingContent(
    isCollapsed: Boolean,
    track: String,
    artist: String,
    art: Bitmap?
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

            art.asComposeBitmap()?.let { bitmap ->
                Image(
                    painter = BitmapPainter(bitmap),
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

    BottomNavigation(
        modifier = modifier.height(Res.Dimen.barHeight + WindowInsets.navigationBarHeight)
    ) {
        Res.Array.shelves.forEachIndexed { index, shelfTitle ->
            BottomNavigationItem(
                onClick = { component.onShelfSelect(index) },
                selected = index == activeIndex,
                modifier = Modifier.navigationBarPadding(),
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
