package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

class AuthImpl(
    componentContext: ComponentContext,
    dependencies: Auth.Dependencies
) : Auth, ComponentContext by componentContext {

    override val models: Value<Auth.Model>

}