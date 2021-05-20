package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun ScrobblesContent(
    component: ScrobblesComponent,
    topInset: Dp
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(Res.Dimen.barHeight + topInset),
                title = {
                    Text(
                        modifier = Modifier.padding(top = topInset),
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