package ru.hotmule.lastik.components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.launchInComposition
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.domain.ListItem


@Composable
fun LibraryList(
    modifier: Modifier = Modifier,
    isUpdating: (Boolean) -> Unit,
    displayWidth: Float? = null,
    refresh: suspend () -> Unit,
    scrollingEnabled: Boolean = true,
    itemsFlow: () -> Flow<List<ListItem>>,
) {

    launchInComposition {
        isUpdating.invoke(true)
        try {
            refresh.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isUpdating.invoke(false)
    }

    val items = itemsFlow
        .invoke()
        .collectAsState(initial = listOf())
        .value

    var scrobbleWidth: Float? = null
    if (!items.isNullOrEmpty() && displayWidth != null) {
        items[0].scrobbles?.let {
            scrobbleWidth = displayWidth / it
        }
    }

    LazyColumnFor(
        items = items,
        modifier = modifier,
        itemContent = {
            LibraryListItem(scrobbleWidth = scrobbleWidth, item = it)
        }
    )
}