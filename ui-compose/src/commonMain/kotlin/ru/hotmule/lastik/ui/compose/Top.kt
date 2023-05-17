package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponent.*
import ru.hotmule.lastik.ui.compose.res.Res
import ru.hotmule.lastik.ui.compose.utils.statusBarHeight
import ru.hotmule.lastik.ui.compose.utils.statusBarPadding

@Composable
fun TopContent(
    component: TopComponent,
) {
    Scaffold(
        topBar = {
            LastikTopAppBar(
                component = component,
            )
        },
        content = {
            ShelfContent(component.shelfComponent)
        }
    )
}

@Composable
private fun LastikTopAppBar(
    component: TopComponent,
) {
    val model by component.model.collectAsState(Model())

    TopAppBar(
        modifier = Modifier.height(Res.Dimen.barHeight + WindowInsets.statusBarHeight),
        title = {
            model.shelfIndex?.let {
                Text(
                    modifier = Modifier.statusBarPadding(),
                    text = Res.Array.shelves[it]
                )
            }
        },
        actions = {
            model.periodIndex?.let {
                TextButton(
                    onClick = component::onPeriodsOpen,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .statusBarPadding()
                        .padding(end = 2.dp)
                ) {
                    Text(text = Res.Array.periods[it])
                    Icon(Icons.Rounded.ExpandMore, null)
                }
            }

            PeriodDropDown(
                expanded = model.periodsOpened,
                onDismissRequest = component::onPeriodsClose,
                onPeriodSelect = component::onPeriodSelected
            )
        }
    )
}

@Composable
private fun PeriodDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onPeriodSelect: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        Column {
            Res.Array.periods.forEachIndexed { index, period ->
                DropdownMenuItem(
                    onClick = {
                        onPeriodSelect(index)
                    }
                ) {
                    Text(text = period)
                }
            }
        }
    }
}