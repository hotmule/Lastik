package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory

val settingsComponentModule = DI.Module("settingsComponent") {

    bindFactory<ComponentContext, SettingsComponent> { componentContext ->
        SettingsComponentImpl(di, componentContext)
    }
}