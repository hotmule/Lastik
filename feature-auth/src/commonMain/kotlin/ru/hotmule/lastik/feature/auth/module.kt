package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.direct

val authComponentModule = DI.Module("authComponent") {

    bindFactory<ComponentContext, AuthComponent> { componentContext ->
        AuthComponentImpl(di.direct, componentContext)
    }
}