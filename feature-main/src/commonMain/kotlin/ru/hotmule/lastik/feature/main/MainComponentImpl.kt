package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.factory
import ru.hotmule.lastik.feature.main.MainComponent.Child
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponentParams
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.feature.settings.SettingsComponentParams

internal class MainComponentImpl(
    override val di: DI,
    private val componentContext: ComponentContext
) : MainComponent, DIAware, ComponentContext by componentContext {

    private val scrobbles by factory<ScrobblesComponentParams, ScrobblesComponent>()
    private val settings by factory<SettingsComponentParams, SettingsComponent>()

    private val navigation = StackNavigation<Config>()
    private val _stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Scrobbles,
        handleBackButton = true,
    ) { configuration, componentContext ->
        when (configuration) {
            is Config.Scrobbles -> Child.Scrobbles(scrobbles(ScrobblesComponentParams(
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
        data object Scrobbles : Config()
        @Serializable
        data object Settings : Config()
    }
}
