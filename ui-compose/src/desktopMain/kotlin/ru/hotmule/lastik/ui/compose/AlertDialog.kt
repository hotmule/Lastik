package ru.hotmule.lastik.ui.compose

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun AlertDialog(
    title: @Composable (() -> Unit)?,
    text: @Composable (() -> Unit)?,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)?
) {
    androidx.compose.material.AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        title = title,
        text = text
    )
}