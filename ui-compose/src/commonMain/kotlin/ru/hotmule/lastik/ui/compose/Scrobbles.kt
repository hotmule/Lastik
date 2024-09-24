package ru.hotmule.lastik.ui.compose

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import lastik.ui_compose.generated.resources.Res
import lastik.ui_compose.generated.resources.scrobbles
import org.jetbrains.compose.resources.stringResource
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.ui.compose.common.LastikTopAppBar

@Composable
fun ScrobblesContent(
    component: ScrobblesComponent,
) {
    Scaffold(
        topBar = {
            LastikTopAppBar(
                title = stringResource(Res.string.scrobbles)
            )
        },
        content = {
            ShelfContent(component.shelfComponent)
        }
    )
}