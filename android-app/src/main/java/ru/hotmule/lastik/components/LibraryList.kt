package ru.hotmule.lastik.components

import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.launchInComposition
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.domain.ListItem

@ExperimentalLazyDsl
@Composable
fun LibraryList(
    modifier: Modifier = Modifier,
    isUpdating: (Boolean) -> Unit,
    displayWidth: Float? = null,
    refresh: suspend () -> Unit,
    itemsFlow: () -> Flow<List<ListItem>>,
    header: @Composable (() -> Unit)? = null
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

    LazyColumn(
        modifier = modifier,
        content = {
            header?.let { item { it.invoke() } }
            items(
                items = items,
                itemContent = { item ->
                    LibraryListItem(scrobbleWidth = scrobbleWidth, item = item)
                }
            )
        }
    )
}