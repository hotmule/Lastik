package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface Auth {

    val models: Value<Model>

    sealed class Output {
        object SignedIn : Output()
    }

    data class Model(
        val login: String,
        val password: String
    )

    interface Dependencies {

    }
}

fun Auth(componentContext: ComponentContext, dependencies: Auth.Dependencies): Auth = AuthImpl(
    componentContext,
    dependencies
)