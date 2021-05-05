package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance
import ru.hotmule.lastik.data.local.LastikDatabase
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient

val libraryComponentModule = DI.Module("libraryComponent") {

    bindFactory<ComponentContext, LibraryComponent> { componentContext ->

        val prefs: PrefsStore by di.instance()
        val remote: LastikHttpClient by di.instance()
        val database: LastikDatabase by di.instance()
        val storeFactory: StoreFactory by di.instance()

        LibraryComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            httpClient = remote,
            database = database,
            prefsStore = prefs
        )
    }
}