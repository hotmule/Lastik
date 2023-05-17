package ru.hotmule.lastik.ui.compose.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

expect fun Modifier.statusBarPadding(): Modifier

expect fun Modifier.navigationBarPadding(): Modifier

expect val WindowInsets.Companion.statusBarHeight: Dp

expect val WindowInsets.Companion.navigationBarHeight: Dp