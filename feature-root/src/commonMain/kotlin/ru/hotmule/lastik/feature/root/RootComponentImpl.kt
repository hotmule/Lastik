package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.main.MainComponent
import ru.hotmule.lastik.feature.root.RootComponent.*

class RootComponentImpl internal constructor(
    componentContext: ComponentContext,
    private val auth: (ComponentContext) -> AuthComponent,
    private val main: (ComponentContext) -> MainComponent
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory
    ) : this(
        componentContext = componentContext,
        auth = { childContext ->
            AuthComponent(
                componentContext = childContext,
                storeFactory = storeFactory
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
                is Config.Auth -> Child.Auth(auth(context))
                is Config.Main -> Child.Main(main(context))
            }
        }
    )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Config : Parcelable {
        @Parcelize
        object Auth : Config()
        @Parcelize
        object Main : Config()
    }
}