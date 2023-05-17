package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.ui.compose.res.Res
import ru.hotmule.lastik.ui.compose.utils.statusBarHeight
import ru.hotmule.lastik.ui.compose.utils.statusBarPadding

@Composable
fun ScrobblesContent(
    component: ScrobblesComponent,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(
                    Res.Dimen.barHeight + WindowInsets.statusBarHeight
                ),
                title = {
                    Text(
                        modifier = Modifier.statusBarPadding(),
                        text = Res.String.scrobbles
                    )
                }
            )
        },
        content = {
            ShelfContent(component.shelfComponent)
        }
    )
}