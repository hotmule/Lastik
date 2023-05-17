package ru.hotmule.lastik.ui.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import ru.hotmule.lastik.utils.Bitmap

actual fun Bitmap?.asComposeBitmap(): ImageBitmap? = this?.asImageBitmap()

@Composable
actual fun remoteImagePainter(
    url: String
): Painter = rememberAsyncImagePainter(url)