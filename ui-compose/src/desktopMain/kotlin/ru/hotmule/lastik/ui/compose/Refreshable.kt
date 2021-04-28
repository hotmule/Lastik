package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable

@Composable
actual fun Refreshable(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {

}