package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.auth.Auth
import ru.hotmule.lastik.feature.main.Main

interface LastikRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class AuthChild(val component: Auth) : Child()
        data class MainChild(val component: Main) : Child()
    }

    interface Dependencies {

    }
}

fun LastikRoot(
    componentContext: ComponentContext,
    dependencies: LastikRoot.Dependencies
): LastikRoot = LastikRootImpl(
    componentContext = componentContext,
    auth = { childContext ->
        Auth(
            componentContext = childContext,
            dependencies = object: Auth.Dependencies, LastikRoot.Dependencies by dependencies {

            }
        )
    },
    main = { childContext ->
        Main(
            componentContext = childContext,
            dependencies = object: Main.Dependencies, LastikRoot.Dependencies by dependencies {

            }
        )
    }
)