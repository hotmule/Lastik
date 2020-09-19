package ru.hotmule.lastfmclient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LibraryListItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .clickable(onClick = { })
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                asset = Icons.Rounded.Album,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
                ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        Divider()
    }
}