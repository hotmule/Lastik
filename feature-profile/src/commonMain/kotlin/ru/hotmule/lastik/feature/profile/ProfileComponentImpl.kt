package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import org.kodein.di.*
import ru.hotmule.lastik.feature.profile.ProfileComponent.Child
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.settings.SettingsComponentParams
import ru.hotmule.lastik.feature.user.UserComponent
import ru.hotmule.lastik.feature.user.UserComponentParams

internal class ProfileComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : ProfileComponent, DIAware, ComponentContext by componentContext {

    private val user by factory<UserComponentParams, UserComponent>()
    private val settings by factory<SettingsComponentParams, SettingsComponent>()

    private val router = router<Config, Child>(
        initialConfiguration = Config.User,
        handleBackButton = true
    ) { configuration, componentContext ->
        when (configuration) {
            is Config.User -> Child.User(user(UserComponentParams(
                componentContext = componentContext,
                output = { output ->
                    when (output) {
                        UserComponent.Output.SettingsOpened -> goToSettings()
                    }
                }
            )))
            Config.Settings -> Child.Settings(settings(SettingsComponentParams(
                componentContext = componentContext,
                output = { output ->
                    when (output) {
                        SettingsComponent.Output.BackPressed -> goBack()
                    }

                }
            )))
        }
    }

    private fun goToSettings() {
        router.push(Config.Settings)
    }

    private fun goBack() {
        router.pop()
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Config : Parcelable {
        @Parcelize object User : Config()
        @Parcelize object Settings : Config()
    }
}