package ru.hotmule.lastik.ui.compose

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.feature.top.TopComponent

@Composable
fun TopContent(
    component: TopComponent,
    topInset: Dp,
    bottomInset: Dp
) {
    val model by component.model.collectAsState(Model())

    Scaffold(
        content = {
            Children(component.routerState) { child, _ ->
                when (child) {
                    is TopComponent.Child.Shelf -> ShelfContent(child.component, bottomInset)
                }
            }
        }
    )
}