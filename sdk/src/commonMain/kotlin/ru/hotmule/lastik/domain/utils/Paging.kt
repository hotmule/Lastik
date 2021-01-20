package ru.hotmule.lastik.domain.utils

import com.squareup.sqldelight.Query

suspend fun <T> providePage(
    isFirstPage: Boolean,
    getCurrentItemsCount: Query<Long>,
    loadPage: suspend (Int) -> T?,
    deleteOldItems: () -> Unit,
    updateItems: (T) -> Unit
) {
    val count = getCurrentItemsCount.executeAsOne().toInt()

    if (count.rem(50) == 0) {
        loadPage.invoke(
            if (isFirstPage) 1 else count / 50 + 1
        )?.also {
            if (isFirstPage) deleteOldItems.invoke()
            updateItems.invoke(it)
        }
    }
}