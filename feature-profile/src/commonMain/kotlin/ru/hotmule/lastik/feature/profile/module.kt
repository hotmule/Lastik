package ru.hotmule.lastik.feature.profile

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.settings.settingsComponentModule
import ru.hotmule.lastik.feature.user.userComponentModule

val profileComponentModule = DI.Module("profileComponent") {

    import(settingsComponentModule)
    import(userComponentModule)

    bindFactory<ComponentContext, ProfileComponent> { componentContext ->
        ProfileComponentImpl(di, componentContext)
    }
}