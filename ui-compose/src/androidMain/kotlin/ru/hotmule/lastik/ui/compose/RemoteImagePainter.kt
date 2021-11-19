package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberImagePainter

@Composable
actual fun remoteImagePainter(
    url: String
): Painter = rememberImagePainter(url)