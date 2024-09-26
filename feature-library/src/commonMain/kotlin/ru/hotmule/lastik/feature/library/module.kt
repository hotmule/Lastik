package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.app.nowPlayingComponentModule
import ru.hotmule.lastik.feature.main.mainComponentModule
import ru.hotmule.lastik.feature.top.topComponentModule
import ru.hotmule.lastik.feature.profile.profileComponentModule

val libraryComponentModule = DI.Module("libraryComponent") {

    import(nowPlayingComponentModule)
    import(profileComponentModule)
    import(mainComponentModule)
    import(topComponentModule)

    bindFactory<ComponentContext, LibraryComponent> { componentContext ->
        LibraryComponentImpl(di, componentContext)
    }
}
