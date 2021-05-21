package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory

data class SettingsComponentParams(
    val componentContext: ComponentContext,
    val onBack: () -> Unit
)

val settingsComponentModule = DI.Module("settingsComponent") {

    bindFactory<SettingsComponentParams, SettingsComponent> { params ->
        SettingsComponentImpl(di, params.onBack, params.componentContext)
    }
}