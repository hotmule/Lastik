package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
actual fun PeriodDropDown(
    expanded: Boolean,
    onPeriodSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit
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