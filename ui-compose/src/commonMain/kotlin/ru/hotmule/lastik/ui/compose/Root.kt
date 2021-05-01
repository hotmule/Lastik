package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponent.*

@Composable
fun RootContent(
    component: RootComponent,
    topInset: Dp = 0.dp,
    bottomInset: Dp = 0.dp
) {
    Children(component.routerState) { child, _ ->
        when (child) {
            is Child.Library -> LibraryContent(child.component, topInset, bottomInset)
            is Child.Auth -> AuthContent(child.component, topInset, bottomInset)
        }
    }
}