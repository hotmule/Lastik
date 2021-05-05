package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
actual fun remoteImagePainter(
    data: String
): Painter = rememberVectorPainter(1.dp, 1.dp) { _, _ -> }