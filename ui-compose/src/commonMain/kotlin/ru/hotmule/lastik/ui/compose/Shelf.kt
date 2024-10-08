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
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent.*

private val shelfItemHeight = 82.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShelfContent(
    component: ShelfComponent,
    header: @Composable () -> Unit = { },
    onRefreshHeader: () -> Unit = { }
) {
    val model by component.model.collectAsState(Model())
    val pullRefreshState = rememberPullRefreshState(
        refreshing = model.isRefreshing,
        onRefresh = {
            component.onRefreshItems()
            onRefreshHeader()
        }
    )

    BoxWithConstraints(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        val playCount = model.items.firstOrNull()?.playCount?.toInt() ?: 1
        val scrobbleWidth = if (playCount > 1) maxWidth / playCount else 0.dp

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item { header() }

            itemsIndexed(model.items) { index, item ->

                ShelfItemContent(
                    item = item,
                    scrobbleWidth = scrobbleWidth,
                    onLove = component::onMakeLove
                )

                if (index == model.items.lastIndex) {
                    AdditionalProgress(model.isMoreLoading)
                    component.onLoadMoreItems()
                }
            }
        }
        PullRefreshIndicator(
            refreshing = model.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ShelfItemContent(
    item: ShelfItem,
    scrobbleWidth: Dp,
    onLove: (String, String?, Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .height(shelfItemHeight)
            .fillMaxWidth()
    ) {

        item.playCount?.let {
            Surface(
                content = {},
                color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scrobbleWidth * it.toInt())
                    .align(Alignment.CenterEnd)
            )
        }

        Row(modifier = Modifier.fillMaxSize()) {

            item.rank?.let {
                Text(
                    text = it.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .width(52.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            item.loved?.let {
                IconButton(
                    onClick = { onLove(item.title, item.subtitle, it) },
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

            if (item.savedRemote == false) {

                Icon(
                    contentDescription = "savedRemote",
                    imageVector = Icons.Rounded.Sync,
                    tint = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(50.dp)
                        .height(50.dp)
                )

            } else {

                AsyncImage(
                    model = item.image,
                    contentDescription = "artwork",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(50.dp)
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(8))
                        .background(Color.LightGray)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {

                Text(
                    text = item.title,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis
                )

                item.subtitle?.let {

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

        item.hint?.let {
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

@Composable
private fun AdditionalProgress(
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(shelfItemHeight)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
