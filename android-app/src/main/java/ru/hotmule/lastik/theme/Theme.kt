package ru.hotmule.lastik.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//red
val sangria = Color(0xff9d0000)
val crimson = Color(0xffd50000)

//yellow
val sunflower = Color(0xffffc900)

val barHeight = 56.dp

private val LightColors = lightColors(
    primary = sangria
)

private val DarkColors = darkColors(
    primary = crimson
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}