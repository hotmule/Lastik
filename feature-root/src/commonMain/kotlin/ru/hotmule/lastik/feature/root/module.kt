package ru.hotmule.lastik.feature.root

import co.touchlab.kermit.Kermit
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindSingleton
import ru.hotmule.lastik.data.local.localDataModule
import ru.hotmule.lastik.data.prefs.prefsDataModule
import ru.hotmule.lastik.data.remote.remoteDataModule
import ru.hotmule.lastik.feature.auth.authComponentModule
import ru.hotmule.lastik.feature.browser.browserModule
import ru.hotmule.lastik.feature.library.libraryComponentModule

val rootComponentModule = DI.Module("rootComponent") {

    import(browserModule)
    import(localDataModule)
    import(remoteDataModule)
    importOnce(prefsDataModule)

    import(authComponentModule)
    import(libraryComponentModule)

    bindSingleton { Kermit() }
    bindSingleton { DefaultStoreFactory }

    bindFactory<ComponentContext, RootComponent> { componentContext ->
        RootComponentImpl(di, componentContext)
    }
}