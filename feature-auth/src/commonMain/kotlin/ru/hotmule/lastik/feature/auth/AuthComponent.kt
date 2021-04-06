package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext

interface AuthComponent {

    sealed class Output {
        object SignedIn : Output()
    }

    interface Dependencies {

    }
}

fun AuthComponent(
    context: ComponentContext,
    dependencies: AuthComponent.Dependencies
): AuthComponent = AuthComponentImpl(
    context,
    dependencies
)