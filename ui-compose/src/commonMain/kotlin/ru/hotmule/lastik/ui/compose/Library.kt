package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun LibraryContent(
    component: LibraryComponent,
    topInset: Dp,
    bottomInset: Dp
) {
    Scaffold(
        content = {
            LibraryBody(
                routerState = component.routerState,
                topInset = topInset,
                bottomInset = bottomInset
            )
        },
        bottomBar = {
            LibraryBottomBar(
                component = component,
                bottomInset = bottomInset
            )
        }
    )
}

@Composable
private fun LibraryBody(
    routerState: Value<RouterState<*, Child>>,
    topInset: Dp,
    bottomInset: Dp
) {
    Children(routerState) { child, _ ->
        when (child) {
            is Child.Scrobbles -> ScrobblesContent(child.component, topInset, bottomInset)
            is Child.Artists -> TopContent(child.component, topInset, bottomInset)
            is Child.Albums -> TopContent(child.component, topInset, bottomInset)
            is Child.Tracks -> TopContent(child.component, topInset, bottomInset)
            is Child.Profile -> ProfileContent(child.component, topInset, bottomInset)
        }
    }
}

@Composable
private fun LibraryBottomBar(
    component: LibraryComponent,
    bottomInset: Dp
) {
    val activeIndex by component.activeChildIndex.asState()

    BottomNavigation(
        modifier = Modifier.height(Res.Dimen.barHeight + bottomInset)
    ) {
        Res.Array.shelves.forEachIndexed { index, shelfTitle ->
            BottomNavigationItem(
                onClick = { component.onShelfSelect(index) },
                selected = index == activeIndex,
                modifier = Modifier.padding(bottom = bottomInset),
                label = { Text(shelfTitle) },
                icon = {
                    Icon(
                        contentDescription = shelfTitle,
                        imageVector = Res.Array.shelfIcons[index]
                    )
                }
            )
        }
    }
}