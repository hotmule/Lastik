package ru.hotmule.lastik.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import ru.hotmule.lastik.R
import ru.hotmule.lastik.data.local.ListItem
import ru.hotmule.lastik.theme.sunflower
import ru.hotmule.lastik.utlis.toCommasString
import ru.hotmule.lastik.utlis.toDateString

@Composable
fun LibraryListItem(
    item: ListItem,
    modifier: Modifier = Modifier,
    scrobbleWidth: Float? = null,
    loveTrack: (suspend (String, String, Boolean) -> Unit)? = null,
) {

    var trackLoved by remember { mutableStateOf(false) }

    if (trackLoved) {

        val track = item.title
        val artist = item.subtitle
        val loved = item.loved

        if (track != null && artist != null && loved != null) {
            LaunchedEffect(track) {
                try {
                    loveTrack?.invoke(track, artist, !loved)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    trackLoved = false
                }
            }
        }
    }

    Box(
        modifier = modifier
            .height(82.dp)
            .fillMaxWidth()
            .clickable(onClick = { })
    ) {

        with(item) {

            if (nowPlaying == true) {
                Surface(
                    color = sunflower.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxSize()
                ) { }
            }

            if (scrobbleWidth != null && playCount != null) {
                Surface(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width((scrobbleWidth * playCount!!).dp)
                        .align(Alignment.CenterEnd)
                ) { }
            }

            Row(
                modifier = Modifier.fillMaxSize()
            ) {

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
                        onClick = {
                            trackLoved = true
                        },
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

                CoilImage(
                    data = imageUrl ?: "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png",
                    contentScale = ContentScale.Crop,
                    contentDescription = "artwork",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(50.dp)
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(8))
                        .background(Color.LightGray)
                )

                Column(
                    modifier = modifier
                        .weight(1f)
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        )
                        .align(Alignment.CenterVertically)
                ) {

                    title?.let {
                        Text(
                            text = it,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

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

            if (time != null || playCount != null || nowPlaying == true) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        text = when {
                            time != null -> time!!.toDateString("d MMM, HH:mm")
                            playCount != null -> playCount!!.toCommasString() + " " +
                                    stringResource(R.string.scrobbles)
                            else -> stringResource(R.string.scrobbling_now)
                        }
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

@Preview
@Composable
fun ScrobblePreview() = LibraryListItem(
    ListItem(
        imageUrl = "imageUrl",
        title = "Man",
        subtitle = "Skepta",
        loved = false,
        time = 113243214
    )
)

@Preview
@Composable
fun ArtistPreview() = LibraryListItem(
    ListItem(
        rank = 2,
        title = "Skepta",
        playCount = 500
    )
)

@Preview
@Composable
fun AlbumPreview() = LibraryListItem(
    ListItem(
        rank = 20,
        imageUrl = "imageUrl",
        title = "Konnichiva",
        subtitle = "Skepta",
        playCount = 500
    )
)

@Preview
@Composable
fun TrackPreview() = LibraryListItem(
    ListItem(
        rank = 200,
        imageUrl = "imageUrl",
        title = "Shutdown",
        subtitle = "Skepta",
        playCount = 500
    )
)

@Preview
@Composable
fun LovedTrackPreview() = LibraryListItem(
    ListItem(
        loved = true,
        imageUrl = "imageUrl",
        title = "Man",
        subtitle = "Skepta",
        time = 113243214
    )
)