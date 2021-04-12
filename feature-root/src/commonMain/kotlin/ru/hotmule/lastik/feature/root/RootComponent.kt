package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.main.MainComponent

interface RootComponent {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Auth(val component: AuthComponent) : Child()
        data class Main(val component: MainComponent) : Child()
    }
}