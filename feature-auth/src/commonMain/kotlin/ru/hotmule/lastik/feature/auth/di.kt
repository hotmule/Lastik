package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.browser.WebBrowser
import ru.hotmule.lastik.feature.browser.browserModule

val authComponentModule = DI.Module("authComponent") {

    import(browserModule)

    bindFactory<ComponentContext, AuthComponent> { componentContext ->

        val prefs: PrefsStore by di.instance()
        val browser: WebBrowser by di.instance()
        val remote: LastikHttpClient by di.instance()
        val database: LastikDatabase by di.instance()
        val storeFactory: StoreFactory by di.instance()

        AuthComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            httpClient = remote,
            database = database,
            prefs = prefs
        ) { output ->
            when (output) {
                AuthComponent.Output.SignInWithLastFm -> {
                    browser.open(remote.authApi.authUrl)
                }
            }
        }
    }
}