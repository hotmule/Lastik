package ru.hotmule.lastik

import androidx.compose.runtime.Composable
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import ru.hotmule.lastik.theme.AppTheme

@Composable
fun LastikApp(
    sdk: Sdk,
    displayWidth: Float
) {
    ProvideWindowInsets {
        AppTheme {
            Main(sdk, displayWidth)
        }
    }
}