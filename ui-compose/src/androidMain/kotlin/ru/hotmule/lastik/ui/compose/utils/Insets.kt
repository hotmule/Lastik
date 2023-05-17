package ru.hotmule.lastik.ui.compose.utils

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

actual fun Modifier.statusBarPadding(): Modifier = statusBarsPadding()

actual fun Modifier.navigationBarPadding(): Modifier = navigationBarsPadding()

actual val WindowInsets.Companion.statusBarHeight: Dp
    @Composable
    get() = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

actual val WindowInsets.Companion.navigationBarHeight: Dp
    @Composable
    get() = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()