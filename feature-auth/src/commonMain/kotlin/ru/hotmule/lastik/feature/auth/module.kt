package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.direct
import ru.hotmule.lastik.feature.browser.browserModule

val authComponentModule = DI.Module("authComponent") {

    import(browserModule)

    bindFactory<ComponentContext, AuthComponent> { componentContext ->
        AuthComponentImpl(di.direct, componentContext)
    }
}