package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import org.kodein.di.*
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.root.RootComponent.Child
import ru.hotmule.lastik.feature.root.store.RootStore
import ru.hotmule.lastik.feature.root.store.RootStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class RootComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : RootComponent, DIAware, ComponentContext by componentContext {

    private val library by factory<ComponentContext, LibraryComponent>()
    private val auth by factory<ComponentContext, AuthComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.Library,
        handleBackButton = true
    ) { configuration, componentContext ->
        when (configuration) {
            is Config.Library -> Child.Library(library(componentContext))
            is Config.Auth -> Child.Auth(auth(componentContext))
        }
    }

    private val store = instanceKeeper.getStore {
        RootStoreFactory(
            storeFactory = direct.instance(),
            prefsStore = direct.instance(),
            queries = direct.instance(),
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

    private inline fun <reified T : Child> setConfig(
        config: Config
    ) {
        with(router) {
            if (state.value.activeChild.instance !is T) {
                replaceCurrent(config)
            }
        }
    }
}