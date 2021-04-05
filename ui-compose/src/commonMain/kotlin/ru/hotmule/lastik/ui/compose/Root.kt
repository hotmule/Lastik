package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.root.LastikRoot

@Composable
fun LastikRoot(component: LastikRoot) {
    Children(component.routerState) { child, _ ->
        when (child) {
            is LastikRoot.Child.MainChild -> Main(child.component)
            is LastikRoot.Child.AuthChild -> Auth(child.component)
        }
    }
}