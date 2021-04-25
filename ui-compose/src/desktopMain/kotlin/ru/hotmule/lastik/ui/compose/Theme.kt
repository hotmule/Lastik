package ru.hotmule.lastik.ui.compose

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.runtime.Composable
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun DesktopLastikTheme(
    content: @Composable () -> Unit
) {
    DesktopMaterialTheme(
        colors = Res.Color.lights,
        content = content
    )
}