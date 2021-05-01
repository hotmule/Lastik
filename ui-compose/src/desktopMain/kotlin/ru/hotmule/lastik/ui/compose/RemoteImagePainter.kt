package ru.hotmule.lastik.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale

@Composable
actual fun remoteImagePainter(
    data: String
): Painter = rememberVectorPainter(Icons.Rounded.Album)