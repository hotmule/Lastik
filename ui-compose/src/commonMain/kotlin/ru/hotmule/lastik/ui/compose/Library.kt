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
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.*

@Composable
fun LibraryContent(
    component: LibraryComponent,
    topInset: Dp,
    bottomInset: Dp
) {
    val model by component.model.collectAsState(Model())

    Scaffold(
        topBar = {
            Box {
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
                                Text(text = "All time")
                                Icon(Icons.Rounded.ExpandMore, null)
                            }

                            PeriodDropDown(
                                expanded = model.periodsOpened,
                                onDismissRequest = component::onPeriodSelectClose,
                                onPeriodSelect = component::onShelfSelect
                            )
                        }

                        if (model.logOutAllowed) {

                            IconButton(
                                modifier = Modifier.padding(top = topInset),
                                onClick = { component.onLogOut() }
                            ) {
                                Icon(Icons.Rounded.ExitToApp, "logOut")
                            }
                        }
                    }
                )
            }
        },
        content = {
            Children(component.routerState) { child, _ ->
                ShelfContent(
                    when (child) {
                        is Child.Scrobbles -> child.component
                        is Child.Artists -> child.component
                        is Child.Albums -> child.component
                        is Child.Tracks -> child.component
                        is Child.Profile -> child.component
                    }
                )
            }
        },
        bottomBar = {
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
    )
}

@Composable
expect fun PeriodDropDown(
    expanded: Boolean,
    onPeriodSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit
)