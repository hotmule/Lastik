package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.ProfileComponent.*

@Composable
fun ProfileContent(
    component: ProfileComponent,
) {
    Children(component.stack) {
        it.instance.let { child ->
            when (child) {
                is Child.User -> UserContent(child.component)
                is Child.Settings -> SettingsContent(child.component)
            }
        }
    }
}