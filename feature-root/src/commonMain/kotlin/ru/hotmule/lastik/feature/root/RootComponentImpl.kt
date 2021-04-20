package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.*
import com.arkivanov.decompose.lifecycle.doOnDestroy
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.auth.AuthComponentImpl
import ru.hotmule.lastik.feature.main.MainComponent
import ru.hotmule.lastik.feature.root.RootComponent.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.WebBrowser

class RootComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val httpClient: LastikHttpClient,
    private val prefsStore: PrefsStore,
    private val webBrowser: WebBrowser,
    private val auth: (ComponentContext, (AuthComponent.Output) -> Unit) -> AuthComponent,
    private val main: (ComponentContext) -> MainComponent,
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        prefsStore: PrefsStore,
        webBrowser: WebBrowser
    ) : this(
        componentContext = componentContext,
        httpClient = httpClient,
        prefsStore = prefsStore,
        webBrowser = webBrowser,
        auth = { childContext, output ->
            AuthComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                prefs = prefsStore,
                output = output
            )
        },
        main = { childContext ->
            MainComponent(
                context = childContext
            )
        }
    )

    private val router = router<Config, Child>(
        initialConfiguration = Config.Main,
        handleBackButton = true,
        componentFactory = { config, context ->
            when (config) {
                is Config.Main -> Child.Main(main(context))
                is Config.Auth -> Child.Auth(auth(context) { output ->
                    when (output) {
                        AuthComponent.Output.SignInWithLastFm -> {
                            webBrowser.open(httpClient.authUrl)
                        }
                    }
                })
            }
        }
    )

    override val routerState: Value<RouterState<*, Child>> = router.state
    private val componentScope = CoroutineScope(AppCoroutineDispatcher.Main)

    init {

        componentScope.launch {
            prefsStore.isSessionActive.collect { isActive ->
                if (isActive)
                    setConfig<Child.Main>(Config.Main)
                else
                    setConfig<Child.Auth>(Config.Auth)
            }
        }

        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }

    private inline fun <reified T: Child> setConfig(
        config: Config
    ) {
        with (router) {
            if (state.value.activeChild.component !is T) {
                replaceCurrent(config)
            }
        }
    }

    override fun onTokenUrlReceived(url: String) {
        if (url.contains("token")) {
            prefsStore.token = url.substringAfter("token=")
        }
    }

    private sealed class Config : Parcelable {
        @Parcelize object Auth : Config()
        @Parcelize object Main : Config()
    }
}