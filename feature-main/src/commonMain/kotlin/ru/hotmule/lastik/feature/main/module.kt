package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.scrobbles.scrobblesComponentModule
import ru.hotmule.lastik.feature.settings.settingsComponentModule

val mainComponentModule = DI.Module("mainComponent") {

    import(scrobblesComponentModule)
    import(settingsComponentModule)

    bindFactory<ComponentContext, MainComponent> { componentContext ->
        MainComponentImpl(di, componentContext)
    }
}
