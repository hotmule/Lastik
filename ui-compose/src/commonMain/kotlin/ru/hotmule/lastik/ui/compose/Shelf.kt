package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun ShelfContent(
    component: ShelfComponent,
    modifier: Modifier
) {
    val model by component.model.collectAsState(Model())

    Refreshable(
        isRefreshing = model.isRefreshing,
        onRefresh = component::onRefreshItems
    ) {

        /*
        var scrobbleWidth: Float? = null
        if (!items.isNullOrEmpty()) {
            items[0].playCount?.let {
                scrobbleWidth = displayWidth / it
            }
        }
        */

        LazyColumn(modifier = modifier) {
            itemsIndexed(model.items) { index, item ->

                /*
                if (currentSection == LibrarySection.Profile) {
                    item {
                        ProfileHeader(
                            interactor = sdk.profileInteractor
                        )
                    }
                }
                */

                ShelfItemContent(item)

                if (index == model.items.lastIndex) {
                    AdditionalProgress(model.isMoreLoading)
                    component.onLoadMoreItems()
                }
            }
        }
    }
}

@Composable
private fun ShelfItemContent(
    item: ShelfItem
) {
    Box(
        modifier = Modifier
            .height(82.dp)
            .fillMaxWidth()
    ) {

        with(item) {

            if (highlighted) {
                Surface(
                    color = Res.Color.sunflower.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxSize()
                ) {}
            }

            /*
            if (scrobbleWidth != null && playCount != null) {
                Surface(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width((scrobbleWidth * playCount!!).dp)
                        .align(Alignment.CenterEnd)
                ) { }
            }
             */

            Row(modifier = Modifier.fillMaxSize()) {

                rank?.let {
                    Text(
                        text = it.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .width(52.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                loved?.let {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                            contentDescription = "love",
                            imageVector = if (it)
                                Icons.Rounded.Favorite
                            else
                                Icons.Rounded.FavoriteBorder
                        )
                    }
                }

                Image(
                    painter = remoteImagePainter(image),
                    contentDescription = "artwork",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(50.dp)
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(8))
                        .background(Color.LightGray)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterVertically)
                ) {

                    Text(
                        text = title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis
                    )

                    subtitle?.let {

                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            hint?.let {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun AdditionalProgress(
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
expect fun Refreshable(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
)

@Composable
expect fun remoteImagePainter(data: String) : Painter