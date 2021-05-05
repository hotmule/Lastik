package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.profile.profileComponentModule
import ru.hotmule.lastik.feature.scrobbles.scrobblesComponentModule
import ru.hotmule.lastik.feature.top.topComponentModule

val libraryComponentModule = DI.Module("libraryComponent") {

    import(scrobblesComponentModule)
    import(profileComponentModule)
    import(topComponentModule)

    bindFactory<ComponentContext, LibraryComponent> { componentContext ->
        LibraryComponentImpl(di, componentContext)
    }
}