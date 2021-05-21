package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.settings.SettingsComponent.*

data class SettingsComponentParams(
    val output: (Output) -> Unit,
    val componentContext: ComponentContext
)

val settingsComponentModule = DI.Module("settingsComponent") {

    bindFactory<SettingsComponentParams, SettingsComponent> { params ->
        SettingsComponentImpl(di, params.output, params.componentContext)
    }
}