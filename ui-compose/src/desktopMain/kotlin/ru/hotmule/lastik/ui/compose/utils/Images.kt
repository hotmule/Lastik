package ru.hotmule.lastik.ui.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import ru.hotmule.lastik.utils.Bitmap
import java.net.URL

actual fun Bitmap?.asComposeBitmap(): ImageBitmap? = null

@Composable
actual fun remoteImagePainter(
    url: String
): Painter = BitmapPainter(
    try {
        Image.makeFromEncoded(URL(url).readBytes()).toComposeImageBitmap()
    } catch (e: Exception) {
        ImageBitmap(1, 1)
    }
)