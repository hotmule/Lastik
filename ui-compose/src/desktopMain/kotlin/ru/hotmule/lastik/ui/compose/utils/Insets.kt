package ru.hotmule.lastik.ui.compose.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual fun Modifier.statusBarPadding(): Modifier = Modifier

actual fun Modifier.navigationBarPadding(): Modifier = Modifier

actual val WindowInsets.Companion.statusBarHeight: Dp
    get() = 0.dp

actual val WindowInsets.Companion.navigationBarHeight: Dp
    get() = 0.dp