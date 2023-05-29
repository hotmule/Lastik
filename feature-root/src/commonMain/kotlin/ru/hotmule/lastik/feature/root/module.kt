package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindSingleton
import org.kodein.di.direct
import org.kodein.di.factory
import ru.hotmule.lastik.data.local.localDataModule
import ru.hotmule.lastik.data.sdk.sdkDataModule
import ru.hotmule.lastik.data.remote.remoteDataModule
import ru.hotmule.lastik.feature.auth.authComponentModule
import ru.hotmule.lastik.feature.library.libraryComponentModule
import kotlin.native.concurrent.ThreadLocal

val rootComponentModule = DI.Module("rootComponent") {

    import(localDataModule)
    import(remoteDataModule)
    importOnce(sdkDataModule)

    import(authComponentModule)
    import(libraryComponentModule)

    bindSingleton<StoreFactory> { DefaultStoreFactory() }

    bindFactory<ComponentContext, RootComponent> { componentContext ->
        RootComponentImpl(di, componentContext)
    }
}

@ThreadLocal
object CommonInjector {
    private val di by DI.lazy { import(rootComponentModule) }
    fun provideRoot() = di.direct.factory<ComponentContext, RootComponent>()
}
