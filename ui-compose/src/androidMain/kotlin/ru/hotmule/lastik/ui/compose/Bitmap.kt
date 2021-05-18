package ru.hotmule.lastik.ui.compose

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import ru.hotmule.lastik.utils.Bitmap

actual fun Bitmap?.asComposeBitmap(): ImageBitmap? = this?.asImageBitmap()