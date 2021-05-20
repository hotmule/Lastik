package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun TopContent(
    component: TopComponent,
    topInset: Dp
) {
    Scaffold(
        topBar = {
            LastikTopAppBar(
                component = component,
                topInset = topInset
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
    topInset: Dp
) {
    val model by component.model.collectAsState(Model())

    TopAppBar(
        modifier = Modifier.height(Res.Dimen.barHeight + topInset),
        title = {
            model.shelfIndex?.let {
                Text(
                    modifier = Modifier.padding(top = topInset),
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
                    modifier = Modifier.padding(
                        top = topInset,
                        end = 2.dp
                    )
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
expect fun PeriodDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onPeriodSelect: (Int) -> Unit
)