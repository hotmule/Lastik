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
import lastik.ui_compose.generated.resources.Res
import lastik.ui_compose.generated.resources.periods
import lastik.ui_compose.generated.resources.shelves
import org.jetbrains.compose.resources.stringArrayResource
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.top.TopComponent.*
import ru.hotmule.lastik.ui.compose.common.LastikTopAppBar

@Composable
fun TopContent(
    component: TopComponent,
) {
    Scaffold(
        topBar = {
            PeriodsAppBar(component)
        },
        content = {
            ShelfContent(component.shelfComponent)
        }
    )
}

@Composable
private fun PeriodsAppBar(
    component: TopComponent,
) {
    val model by component.model.collectAsState(Model())
    val shelfIndex = model.shelfIndex
    val periodIndex = model.periodIndex

    LastikTopAppBar(
        title = if (shelfIndex != null) {
            stringArrayResource(Res.array.shelves)[shelfIndex]
        } else {
            ""
        },
        actions = {
            if (periodIndex != null) {
                PeriodsDropDownButton(
                    periodIndex = periodIndex,
                    isExpanded = model.periodsOpened,
                    onClick = component::onPeriodsOpen,
                    onDismissRequest = component::onPeriodsClose,
                    onPeriodSelect = component::onPeriodSelected,
                )
            }
        }
    )
}

@Composable
private fun PeriodsDropDownButton(
    periodIndex: Int,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onPeriodSelect: (Int) -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        ),
        modifier = Modifier
            .statusBarsPadding()
            .padding(end = 2.dp)
    ) {
        Text(text = stringArrayResource(Res.array.periods)[periodIndex])
        Icon(Icons.Rounded.ExpandMore, null)
    }
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest
    ) {
        Column {
            stringArrayResource(Res.array.periods).forEachIndexed { index, period ->
                DropdownMenuItem(
                    onClick = { onPeriodSelect(index) }
                ) {
                    Text(text = period)
                }
            }
        }
    }
}