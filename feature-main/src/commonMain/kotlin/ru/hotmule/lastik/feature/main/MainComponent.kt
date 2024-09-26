package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.settings.SettingsComponent

interface MainComponent {

    sealed class Child {
        data class Scrobbles(val component: ScrobblesComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
    }

    val stack: Value<ChildStack<*, Child>>
}
