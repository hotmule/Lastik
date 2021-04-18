package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.auth.AuthComponentImpl
import ru.hotmule.lastik.feature.main.MainComponent
import ru.hotmule.lastik.feature.root.RootComponent.*
import ru.hotmule.lastik.utils.WebBrowser

class RootComponentImpl internal constructor(
    private val webBrowser: WebBrowser,
    private val componentContext: ComponentContext,
    private val auth: (ComponentContext, (AuthComponent.Output) -> Unit) -> AuthComponentImpl,
    private val main: (ComponentContext) -> MainComponent,
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        prefsStore: PrefsStore,
        webBrowser: WebBrowser
    ) : this(
        webBrowser = webBrowser,
        componentContext = componentContext,
        auth = { childContext, output ->
            AuthComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                output = output,
                prefs = prefsStore
            )
        },
        main = { childContext ->
            MainComponent(
                context = childContext
            )
        }
    )

    private val router = router<Config, Child>(
        initialConfiguration = Config.Auth,
        handleBackButton = true,
        componentFactory = { config, context ->
            when (config) {
                is Config.Main -> Child.Main(main(context))
                is Config.Auth -> Child.Auth(auth(context) { output ->
                    when (output) {
                        is AuthComponent.Output.SignInWithLastFmSelected -> {
                            webBrowser.open(output.url)
                        }
                    }
                })
            }
        }
    )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Config : Parcelable {
        @Parcelize object Auth : Config()
        @Parcelize object Main : Config()
    }

    override fun onTokenUrlReceived(url: String) {
        val currentChild = routerState.value.activeChild.component
        if (currentChild is Child.Auth) {
            currentChild.component.onTokenUrlReceived(url)
        }
    }
}