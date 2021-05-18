package ru.hotmule.lastik.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.ui.compose.res.Res
import ru.hotmule.lastik.utils.Bitmap

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
            sheetPeekHeight = if (model.isPlaying) Res.Dimen.shelfItemHeight else 0.dp,
            sheetContent = {
                NowPlayingContent(
                    track = model.track,
                    artist = model.artist,
                    art = model.art,
                    isCollapsed = bottomSheetScaffoldState.bottomSheetState.isCollapsed
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun NowPlayingContent(
    track: String,
    artist: String,
    art: Bitmap?,
    isCollapsed: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(if (isCollapsed) Alignment.TopCenter else Alignment.Center)
                .height(Res.Dimen.shelfItemHeight)
        ) {

            if (isCollapsed) {
                Image(
                    imageVector = Icons.Rounded.Equalizer,
                    contentDescription = "Now playing",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    modifier = Modifier
                        .padding(14.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            art.asComposeBitmap()?.let {
                Image(
                    painter = BitmapPainter(it),
                    contentDescription = "artwork",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(if (isCollapsed) 50.dp else 100.dp)
                        .height(if (isCollapsed) 50.dp else 100.dp)
                        .clip(shape = RoundedCornerShape(8))
                )
            }

            if (isCollapsed) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp)
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

expect fun Bitmap?.asComposeBitmap(): ImageBitmap?