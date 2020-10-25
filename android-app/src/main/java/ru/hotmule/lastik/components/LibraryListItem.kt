package ru.hotmule.lastik.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
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
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import ru.hotmule.lastik.R
import ru.hotmule.lastik.domain.ListItem
import ru.hotmule.lastik.theme.sunflower
import ru.hotmule.lastik.utlis.toCommasString
import ru.hotmule.lastik.utlis.toDateString

@Composable
fun LibraryListItem(
    item: ListItem,
    modifier: Modifier = Modifier,
    scrobbleWidth: Float? = null,
) {
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

            if (scrobbleWidth != null && scrobbles != null) {
                Surface(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    modifier = Modifier
                        .fillMaxHeight()
                        .preferredWidth((scrobbleWidth * scrobbles!!).dp)
                        .align(Alignment.CenterEnd)
                ) { }
            }

            Row(
                modifier = Modifier.fillMaxSize()
            ) {

                position?.let {
                    Text(
                        text = it.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .preferredWidth(52.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                loved?.let {
                    IconButton(
                        onClick = { },
                        icon = {
                            Image(
                                asset = if (it)
                                    Icons.Rounded.Favorite
                                else
                                    Icons.Rounded.FavoriteBorder,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                            )
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                CoilImage(
                    data = imageUrl ?: "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .preferredWidth(50.dp)
                        .preferredHeight(50.dp)
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
                        ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium) {
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

            if (time != null || scrobbles != null || nowPlaying == true) {
                ProvideEmphasis(
                    emphasis = AmbientEmphasisLevels.current.medium
                ) {
                    Text(
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        text = when {
                            time != null -> time!!.toDateString("d MMM, HH:mm")
                            scrobbles != null -> scrobbles!!.toCommasString() + " " +
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
        position = 2,
        title = "Skepta",
        scrobbles = 500
    )
)

@Preview
@Composable
fun AlbumPreview() = LibraryListItem(
    ListItem(
        position = 20,
        imageUrl = "imageUrl",
        title = "Konnichiva",
        subtitle = "Skepta",
        scrobbles = 500
    )
)

@Preview
@Composable
fun TrackPreview() = LibraryListItem(
    ListItem(
        position = 200,
        imageUrl = "imageUrl",
        title = "Shutdown",
        subtitle = "Skepta",
        scrobbles = 500
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