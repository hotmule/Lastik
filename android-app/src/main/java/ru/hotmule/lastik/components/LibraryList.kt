package ru.hotmule.lastik.components

import androidx.compose.foundation.ScrollableColumn
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

    ScrollableColumn(
        modifier = modifier,
        isScrollEnabled = scrollingEnabled
    ) {
        items.forEach {
            LibraryListItem(scrobbleWidth = scrobbleWidth, item = it)
        }
    }

    // LazyColumn performance is worse than ScrollableColumn
    //
    // LazyColumnFor(
    //     modifier = modifier,
    //     items = items,
    //     itemContent = { LibraryListItem(item = it) }
    // )
}