package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.auth.AuthComponentImpl
import ru.hotmule.lastik.feature.main.MainComponent
import ru.hotmule.lastik.feature.root.RootComponent.*
import ru.hotmule.lastik.utils.WebBrowser
import ru.hotmule.lastik.utils.componentCoroutineScope

class RootComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val prefsStore: PrefsStore,
    private val auth: (ComponentContext) -> AuthComponentImpl,
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
        prefsStore = prefsStore,
        auth = { childContext ->
            AuthComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                webBrowser = webBrowser,
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
        initialConfiguration = Config.Main,
        handleBackButton = true,
        componentFactory = { config, context ->
            when (config) {
                is Config.Main -> Child.Main(main(context))
                is Config.Auth -> Child.Auth(auth(context))
            }
        }
    )

    override val routerState: Value<RouterState<*, Child>> = router.state

    init {

        lifecycle.componentCoroutineScope.launch {
            prefsStore.isSessionActive.collect { isActive ->
                router.state.value.activeChild.configuration.also { config ->
                    when {
                        config !is Config.Auth && !isActive -> router.replaceCurrent(Config.Auth)
                        config !is Config.Main && isActive -> router.replaceCurrent(Config.Main)
                    }
                }
            }
        }
    }

    private sealed class Config : Parcelable {
        @Parcelize object Auth : Config()
        @Parcelize object Main : Config()
    }

    override fun onTokenUrlReceived(url: String) {
        val child = routerState.value.activeChild.component
        if (child is Child.Auth) {
            child.component.onTokenUrlReceived(url)
        }
    }
}