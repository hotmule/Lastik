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
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.auth.AuthComponentImpl
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.library.LibraryComponentImpl
import ru.hotmule.lastik.feature.root.RootComponent.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.WebBrowser

class RootComponentImpl internal constructor(
    private val componentContext: ComponentContext,
    private val httpClient: LastikHttpClient,
    private val database: LastikDatabase,
    private val prefsStore: PrefsStore,
    private val webBrowser: WebBrowser,
    private val auth: (ComponentContext, (AuthComponent.Output) -> Unit) -> AuthComponent,
    private val library: (ComponentContext) -> LibraryComponent,
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        httpClient: LastikHttpClient,
        database: LastikDatabase,
        prefsStore: PrefsStore,
        webBrowser: WebBrowser
    ) : this(
        componentContext = componentContext,
        httpClient = httpClient,
        database = database,
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
        library = { childContext ->
            LibraryComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                httpClient = httpClient,
                database = database,
                prefsStore = prefsStore
            )
        }
    )

    private val router = router<Config, Child>(
        initialConfiguration = Config.Library,
        handleBackButton = true,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Config.Library -> Child.Library(library(componentContext))
                is Config.Auth -> Child.Auth(auth(componentContext) { output ->
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
                    setConfig<Child.Library>(Config.Library)
                else {
                    database.profileQueries.deleteAll()
                    setConfig<Child.Auth>(Config.Auth)
                }
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
        @Parcelize object Library : Config()
        @Parcelize object Auth : Config()
    }
}