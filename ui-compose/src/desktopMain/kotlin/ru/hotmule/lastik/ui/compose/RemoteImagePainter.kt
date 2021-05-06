package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.skija.Image
import org.kodein.di.compose.LocalDI
import org.kodein.di.direct
import org.kodein.di.instance
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

@Composable
actual fun remoteImagePainter(
    url: String
): Painter {

    val client = LocalDI.current.direct.instance<HttpClient>()

    val bitmap = runBlocking(AppCoroutineDispatcher.IO) {
        try {
            Image.makeFromEncoded(client.get<ByteArray>(url)).asImageBitmap()
        } catch (e: Exception) {
            ImageBitmap(1, 1)
        }
    }

    return BitmapPainter(bitmap)
}