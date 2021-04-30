package ru.hotmule.lastik.ui.compose

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent

@Composable
fun ScrobblesContent(
    component: ScrobblesComponent,
    topInset: Dp,
    bottomInset: Dp
) {
    Scaffold(
        content = {
            Children(component.routerState) { child, _ ->
                when (child) {
                    is ScrobblesComponent.Child.Shelf -> ShelfContent(child.component, bottomInset)
                }
            }
        }
    )
}