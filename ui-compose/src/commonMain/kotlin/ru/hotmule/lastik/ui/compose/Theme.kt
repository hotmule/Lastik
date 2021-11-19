package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun LastikTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) Res.Color.darks else Res.Color.lights,
        content = content
    )
}