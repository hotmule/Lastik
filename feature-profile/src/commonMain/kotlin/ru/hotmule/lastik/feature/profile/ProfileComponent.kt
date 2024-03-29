package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.user.UserComponent

interface ProfileComponent {

    sealed class Child {
        data class User(val component: UserComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
    }

    val stack: Value<ChildStack<*, Child>>
}