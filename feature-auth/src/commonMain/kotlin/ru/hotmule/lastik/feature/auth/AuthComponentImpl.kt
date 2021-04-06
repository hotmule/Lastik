package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext

class AuthComponentImpl(
    context: ComponentContext,
    dependencies: AuthComponent.Dependencies
) : AuthComponent, ComponentContext by context {

}