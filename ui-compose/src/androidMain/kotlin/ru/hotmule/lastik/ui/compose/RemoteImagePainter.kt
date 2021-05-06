package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.google.accompanist.coil.rememberCoilPainter

@Composable
actual fun remoteImagePainter(
    url: String
): Painter = rememberCoilPainter(url)