package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun SettingsContent(
    component: SettingsComponent,
    topInset: Dp
) {
    Scaffold(
        topBar = {
            SettingsTopBar(
                topInset = topInset,
                onPop = component::onBackPressed
            )
        },
        content = {

        }
    )
}

@Composable
private fun SettingsTopBar(
    topInset: Dp,
    onPop: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(Res.Dimen.barHeight + topInset),
        title = {
            Text(
                modifier = Modifier.padding(top = topInset),
                text = Res.Array.profileMenu.first()
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(top = topInset),
                onClick = onPop
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Pop",
                    tint = Color.White
                )
            }
        }
    )
}