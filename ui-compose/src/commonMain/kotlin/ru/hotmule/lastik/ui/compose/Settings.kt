package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.settings.SettingsComponent.*
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
            SettingsBody(component)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsBody(
    component: SettingsComponent
) {
    val model by component.model.collectAsState(Model())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (model.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyVerticalGrid(
            cells = GridCells.Adaptive(124.dp),
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

                appPackage.bitmap.asComposeBitmap()?.let {

                    Image(
                        painter = BitmapPainter(it),
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