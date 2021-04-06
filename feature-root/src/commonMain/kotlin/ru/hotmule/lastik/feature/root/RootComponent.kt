package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
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

    interface Dependencies {

    }
}

fun RootComponent(
    context: ComponentContext,
    dependencies: RootComponent.Dependencies
): RootComponent = RootComponentImpl(
    context = context,
    auth = { childContext ->
        AuthComponent(
            context = childContext,
            dependencies = object: AuthComponent.Dependencies, RootComponent.Dependencies by dependencies {

            }
        )
    },
    main = { childContext ->
        MainComponent(
            context = childContext,
            dependencies = object: MainComponent.Dependencies, RootComponent.Dependencies by dependencies {

            }
        )
    }
)