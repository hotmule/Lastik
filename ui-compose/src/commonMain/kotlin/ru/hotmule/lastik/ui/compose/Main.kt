package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.hotmule.lastik.feature.main.MainComponent

@Composable
fun MainContent(
    component: MainComponent,
) {
    Children(component.stack) {
        it.instance.let { child ->
            when (child) {
                is MainComponent.Child.Scrobbles -> ScrobblesContent(child.component)
                is MainComponent.Child.Settings -> SettingsContent(child.component)
            }
        }
    }
}
