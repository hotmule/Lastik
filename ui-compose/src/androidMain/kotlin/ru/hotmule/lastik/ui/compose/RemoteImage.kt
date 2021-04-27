package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.google.accompanist.coil.CoilImage

@Composable
actual fun RemoteImage(
    data: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    CoilImage(
        data = data,
        contentDescription = "artwork",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}