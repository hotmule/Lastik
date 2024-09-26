package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            ScrobblesTopBar(
                onOpenSettings = component::onOpenSettings,
            )
        },
        content = {
            ShelfContent(component.shelfComponent)
        }
    )
}

@Composable
private fun ScrobblesTopBar(
    onOpenSettings: () -> Unit,
) {
    LastikTopAppBar(
        title = stringResource(Res.string.scrobbles),
        actions = {
            IconButton(
                modifier = Modifier.statusBarsPadding(),
                onClick = onOpenSettings,
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                )
            }
        }
    )
}
