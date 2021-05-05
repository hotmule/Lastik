package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.factory
import org.kodein.di.instance
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.root.RootComponent.*
import ru.hotmule.lastik.feature.root.store.RootStore
import ru.hotmule.lastik.feature.root.store.RootStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class RootComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : RootComponent, DIAware, ComponentContext by componentContext {

    private val library by factory<ComponentContext,  LibraryComponent>()
    private val auth by factory<ComponentContext, AuthComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Library,
        handleBackButton = true,
        componentFactory = { configuration, componentContext ->
            when (configuration) {
                is Config.Library -> Child.Library(library(componentContext))
                is Config.Auth -> Child.Auth(auth(componentContext))
            }
        }
    )

    private val store = instanceKeeper.getStore {

        val storeFactory: StoreFactory by instance()
        val prefsStore: PrefsStore by instance()
        val queries: ProfileQueries by instance()

        RootStoreFactory(
            storeFactory = storeFactory,
            prefsStore = prefsStore,
            queries = queries,
            onSessionChanged = { isActive ->
                if (isActive)
                    setConfig<Child.Library>(Config.Library)
                else
                    setConfig<Child.Auth>(Config.Auth)
            }
        ).create()
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    override fun onUrlReceived(url: String?) {
        store.accept(RootStore.Intent.ProcessUrl(url))
    }

    private sealed class Config : Parcelable {
        @Parcelize object Library : Config()
        @Parcelize object Auth : Config()
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
}