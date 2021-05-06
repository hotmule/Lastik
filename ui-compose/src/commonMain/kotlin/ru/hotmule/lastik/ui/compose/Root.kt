package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import org.kodein.di.DI
import org.kodein.di.compose.withDI
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponent.*

@Composable
fun RootContent(
    di: DI,
    component: RootComponent,
    topInset: Dp = 0.dp,
    bottomInset: Dp = 0.dp
) = withDI(di) {
    Children(component.routerState) {
        it.instance.let { child ->
            when (child) {
                is Child.Library -> LibraryContent(child.component, topInset, bottomInset)
                is Child.Auth -> AuthContent(child.component, topInset, bottomInset)
            }
        }
    }
}