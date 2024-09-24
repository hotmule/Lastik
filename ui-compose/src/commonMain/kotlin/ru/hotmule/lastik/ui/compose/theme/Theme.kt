package ru.hotmule.lastik.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun LastikTheme(
    content: @Composable () -> Unit
) {
    val crimson = Color(0xffd50000)
    val sangria = Color(0xff9d0000)

    MaterialTheme(
        colors = if (isSystemInDarkTheme()) {
            darkColors(primary = crimson)
        } else {
            lightColors(primary = sangria, secondary = crimson)
        },
    ) {
        content()
    }
}