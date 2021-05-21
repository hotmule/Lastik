package ru.hotmule.lastik.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.ProfileComponent.*

@Composable
fun ProfileContent(
    component: ProfileComponent,
    topInset: Dp
) {
    Children(component.routerState) {
        it.instance.let { child ->
            when (child) {
                is Child.User -> UserContent(child.component, topInset)
                is Child.Settings -> SettingsContent(child.component, topInset)
            }
        }
    }
}