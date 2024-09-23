package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.*
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
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

    private val navigation = StackNavigation<Config>()
    private val _stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.User,
        handleBackButton = true,
    ) { configuration, componentContext ->
        when (configuration) {
            is Config.User -> Child.User(user(UserComponentParams(
                componentContext = componentContext,
                onSettingsOpen = ::goToSettings
            )))
            Config.Settings -> Child.Settings(settings(SettingsComponentParams(
                componentContext = componentContext,
                onBack = ::goBack
            )))
        }
    }

    private fun goToSettings() {
        navigation.push(Config.Settings)
    }

    private fun goBack() {
        navigation.pop()
    }

    override val stack: Value<ChildStack<*, Child>> = _stack

    @Serializable
    private sealed class Config {
        @Serializable
        data object User : Config()
        @Serializable
        data object Settings : Config()
    }
}