package ru.hotmule.lastik.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import ru.hotmule.lastik.R
import ru.hotmule.lastik.domain.LibraryListItem

@Composable
fun LibraryListItem(
    item: LibraryListItem,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .clickable(onClick = { })
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        val (
            position,
            like,
            image,
            title,
            subtitle,
            scrobbles,
            time
        ) = createRefs()

        item.position?.let {
            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .preferredWidth(24.dp)
                    .constrainAs(position) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        item.loved?.let {
            Image(
                asset = if (it) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier
                    .clickable(onClick = { })
                    .constrainAs(like) {
                        start.linkTo(position.end, 16.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        item.imageUrl?.let {
            CoilImage(
                data = it,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .clip(shape = RoundedCornerShape(8))
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(
                            if (item.loved != null)
                                like.end
                            else
                                position.end,
                            16.dp
                        )
                    }
            )
        }

        item.title?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(image.end, 16.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(
                            if (item.subtitle != null)
                                subtitle.top
                            else
                                parent.bottom
                        )
                    }
            )
        }

        item.subtitle?.let {
            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .constrainAs(subtitle) {
                            start.linkTo(title.start)
                            top.linkTo(title.bottom, 8.dp)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }

        item.scrobbles?.let {
            Text(
                text = "$it ${stringResource(id = R.string.scrobbles)}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .constrainAs(scrobbles) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        item.time?.let {
            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .constrainAs(time) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }
    }

    Divider(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp
        )
    )
}

@Preview
@Composable
fun ScrobblePreview() = LibraryListItem(
    LibraryListItem(
        imageUrl = "imageUrl",
        title = "Man",
        subtitle = "Skepta",
        time = "21 hours ago"
    )
)

@Preview
@Composable
fun ArtistPreview() = LibraryListItem(
    LibraryListItem(
        position = 2,
        imageUrl = "imageUrl",
        title = "Skepta",
        scrobbles = 500
    )
)

@Preview
@Composable
fun AlbumPreview() = LibraryListItem(
    LibraryListItem(
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
    LibraryListItem(
        position = 200,
        imageUrl = "imageUrl",
        title = "Shutdown",
        subtitle = "Skepta",
        scrobbles = 500
    )
)

@Preview
@Composable
fun LovableTrackPreview() = LibraryListItem(
    LibraryListItem(
        loved = true,
        imageUrl = "imageUrl",
        title = "Man",
        subtitle = "Skepta",
        scrobbles = 500
    )
)