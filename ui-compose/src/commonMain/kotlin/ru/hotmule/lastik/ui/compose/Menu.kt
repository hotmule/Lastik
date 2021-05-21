package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.hotmule.lastik.feature.menu.MenuComponent
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    component: MenuComponent
) {
    Column(
        modifier = modifier
    ) {
        Res.Array.profileMenu.forEachIndexed { index, item ->

            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colors.onBackground
                ),
                onClick = {
                    when (index) {
                        0 -> component.onOpenSettings()
                        else -> component.onLogOut()
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Icon(
                    contentDescription = "menu item $index",
                    imageVector = when (index) {
                        0 -> Icons.Rounded.Tune
                        else -> Icons.Rounded.Logout
                    }
                )
                Text(
                    text = item,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }
        }
    }
}