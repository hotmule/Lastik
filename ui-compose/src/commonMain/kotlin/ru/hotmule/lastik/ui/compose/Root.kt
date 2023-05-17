package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import org.kodein.di.DI
import org.kodein.di.compose.withDI
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponent.*

@Composable
fun RootContent(
    di: DI,
    component: RootComponent,
) = withDI(di) {
    Children(component.stack) {
        it.instance.let { child ->
            when (child) {
                is Child.Library -> LibraryContent(child.component)
                is Child.Auth -> AuthContent(child.component)
            }
        }
    }
}