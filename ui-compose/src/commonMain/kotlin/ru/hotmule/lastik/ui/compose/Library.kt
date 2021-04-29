package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun LibraryContent(
    component: LibraryComponent,
    topInset: Dp,
    bottomInset: Dp
) {
    val model by component.model.collectAsState(Model())

    Scaffold(
        topBar = {
            LibraryTopBar(
                component = component,
                model = model,
                topInset = topInset
            )
        },
        content = {
            LibraryBody(
                routerState = component.routerState,
                bottomInset = bottomInset
            )
        },
        bottomBar = {
            LibraryBottomBar(
                component = component,
                model = model,
                bottomInset = bottomInset
            )
        }
    )
}

@Composable
private fun LibraryTopBar(
    component: LibraryComponent,
    model: Model,
    topInset: Dp
) {
    TopAppBar(
        modifier = Modifier.height(Res.Dimen.barHeight + topInset),
        title = {
            Text(
                modifier = Modifier.padding(top = topInset),
                text = Res.Array.shelves[model.activeShelfIndex]
            )
        },
        actions = {

            if (model.periodSelectable) {

                TextButton(
                    onClick = component::onPeriodSelectOpen,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(
                        top = topInset,
                        end = 2.dp
                    )
                ) {
                    Text(text = Res.Array.periods[model.selectedPeriodIndex])
                    Icon(Icons.Rounded.ExpandMore, null)
                }

                PeriodDropDown(
                    expanded = model.periodsOpened,
                    onDismissRequest = component::onPeriodSelectClose,
                    onPeriodSelect = component::onPeriodSelected
                )
            }

            if (model.logOutAllowed) {

                IconButton(
                    modifier = Modifier.padding(top = topInset),
                    onClick = { component.onLogOut() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = "logOut",
                        tint = Color.White
                    )
                }
            }
        }
    )
}

@Composable
expect fun PeriodDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onPeriodSelect: (Int) -> Unit
)

@Composable
private fun LibraryBody(
    routerState: Value<RouterState<*, Child>>,
    bottomInset: Dp
) {
    Children(routerState) { child, _ ->
        when (child) {
            is Child.Scrobbles -> ShelfContent(child.component, bottomInset)
            is Child.Artists -> ShelfContent(child.component, bottomInset)
            is Child.Albums -> ShelfContent(child.component, bottomInset)
            is Child.Tracks -> ShelfContent(child.component, bottomInset)
            is Child.Profile -> ProfileContent(child.component, bottomInset)
        }
    }
}

@Composable
private fun LibraryBottomBar(
    component: LibraryComponent,
    model: Model,
    bottomInset: Dp
) {
    BottomNavigation(
        modifier = Modifier.height(Res.Dimen.barHeight + bottomInset)
    ) {
        Res.Array.shelves.forEachIndexed { index, shelfTitle ->
            BottomNavigationItem(
                onClick = { component.onShelfSelect(index) },
                selected = index == model.activeShelfIndex,
                modifier = Modifier.padding(bottom = bottomInset),
                label = { Text(shelfTitle) },
                icon = {
                    Icon(
                        contentDescription = shelfTitle,
                        imageVector = Res.Array.shelfIcons[index]
                    )
                }
            )
        }
    }
}