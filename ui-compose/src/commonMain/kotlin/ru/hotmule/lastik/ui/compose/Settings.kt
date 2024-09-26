package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.toBitmap
import lastik.ui_compose.generated.resources.Res
import lastik.ui_compose.generated.resources.scrobble_settings
import org.jetbrains.compose.resources.stringResource
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.ui.compose.common.LastikTopAppBar

@Composable
fun SettingsContent(
    component: SettingsComponent,
) {
    Scaffold(
        topBar = {
            SettingsTopBar(
                onPop = component::onBackPressed
            )
        },
        content = {
            SettingsBody(component)
        }
    )
}

@Composable
private fun SettingsTopBar(
    onPop: () -> Unit
) {
    LastikTopAppBar(
        title = stringResource(Res.string.scrobble_settings),
        navigationIcon = {
            IconButton(
                modifier = Modifier.statusBarsPadding(),
                onClick = onPop
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Pop",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun SettingsBody(
    component: SettingsComponent
) {
    val model by component.model.collectAsState(SettingsComponent.Model())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (model.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(124.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(model.apps) {
                CheckableApp(
                    appPackage = it,
                    onClick = component::onAppClick,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CheckableApp(
    appPackage: SettingsComponent.Package,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { onClick(appPackage.name) }
    ) {
        Box {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                appPackage.icon?.let { icon ->
                    AsyncImage(
                        model = icon.toBitmap(),
                        contentDescription = appPackage.label,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = appPackage.label,
                        maxLines = 1,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
            }

            Checkbox(
                checked = appPackage.isEnabled,
                onCheckedChange = { onClick(appPackage.name) },
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}
