package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.bindFactory
import ru.hotmule.lastik.feature.app.nowPlayingComponentModule
import ru.hotmule.lastik.feature.profile.profileComponentModule
import ru.hotmule.lastik.feature.scrobbles.scrobblesComponentModule
import ru.hotmule.lastik.feature.top.topComponentModule

val libraryComponentModule = DI.Module("libraryComponent") {

    import(nowPlayingComponentModule)
    import(scrobblesComponentModule)
    import(profileComponentModule)
    import(topComponentModule)

    bindFactory<ComponentContext, LibraryComponent> { componentContext ->
        LibraryComponentImpl(di, componentContext)
    }
}