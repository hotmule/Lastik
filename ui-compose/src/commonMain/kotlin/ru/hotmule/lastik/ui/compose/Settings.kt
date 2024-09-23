package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
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
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.settings.SettingsComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

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
    TopAppBar(
        modifier = Modifier.height(
            Res.Dimen.barHeight + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        ),
        title = {
            Text(
                modifier = Modifier.statusBarsPadding(),
                text = Res.String.scrobble_apps
            )
        },
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
    val model by component.model.collectAsState(Model())

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
    appPackage: Package,
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