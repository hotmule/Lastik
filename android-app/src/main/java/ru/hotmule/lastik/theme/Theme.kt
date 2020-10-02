package ru.hotmule.lastik.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val sangria = Color(0xff9d0000)
val scarlet = Color(0xffb90000)
val crimson = Color(0xffd50000)
val habanero = Color(0xfff71414)
val postbox = Color(0xffd92323)
val jam = Color(0xffbd1e1e)

val BarHeight = 56.dp

private val LightColors = lightColors(
    primary = sangria,
    secondary = crimson
)

private val DarkColors = darkColors(
    secondary = crimson
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