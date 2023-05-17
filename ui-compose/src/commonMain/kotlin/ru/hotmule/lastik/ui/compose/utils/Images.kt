package ru.hotmule.lastik.ui.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import ru.hotmule.lastik.utils.Bitmap

expect fun Bitmap?.asComposeBitmap(): ImageBitmap?

@Composable
expect fun remoteImagePainter(url: String): Painter