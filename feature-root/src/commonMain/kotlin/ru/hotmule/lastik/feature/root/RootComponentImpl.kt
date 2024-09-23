package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import kotlinx.serialization.Serializable
import org.kodein.di.*
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.library.LibraryComponent
import ru.hotmule.lastik.feature.root.RootComponent.Child
import ru.hotmule.lastik.feature.root.store.RootStore
import ru.hotmule.lastik.feature.root.store.RootStoreFactory

internal class RootComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : RootComponent, DIAware, ComponentContext by componentContext {

    private val library by factory<ComponentContext, LibraryComponent>()
    private val auth by factory<ComponentContext, AuthComponent>()

    private val navigation = StackNavigation<Config>()
    private val _stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
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

    override val stack: Value<ChildStack<*, Child>> = _stack

    override fun onUrlReceived(url: String?) {
        store.accept(RootStore.Intent.ProcessUrl(url))
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Library : Config()
        @Serializable
        data object Auth : Config()
    }

    private inline fun <reified T : Child> setConfig(
        config: Config
    ) {
        if (_stack.value.active.instance !is T) {
            navigation.replaceCurrent(config)
        }
    }
}