package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.auth.Auth
import ru.hotmule.lastik.feature.main.Main
import ru.hotmule.lastik.feature.root.LastikRoot.*

class LastikRootImpl(
    componentContext: ComponentContext,
    private val auth: (ComponentContext) -> Auth,
    private val main: (ComponentContext) -> Main
) : LastikRoot, ComponentContext by componentContext {

    private val router = router<Config, Child>(
        initialConfiguration = Config.Main,
        handleBackButton = true,
        componentFactory = { config, context ->
            when (config) {
                is Config.Auth -> Child.AuthChild(auth(context))
                is Config.Main -> Child.MainChild(main(context))
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