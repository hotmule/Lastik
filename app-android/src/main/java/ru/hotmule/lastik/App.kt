package ru.hotmule.lastik

import android.app.Application
import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.kodein.di.*
import ru.hotmule.lastik.data.local.localDataModule
import ru.hotmule.lastik.data.prefs.prefsDataModule
import ru.hotmule.lastik.data.remote.remoteDataModule
import ru.hotmule.lastik.feature.browser.browserModule
import ru.hotmule.lastik.feature.root.rootComponentModule

class App : Application(), DIAware {

    override val di by DI.lazy {

        bindInstance<Context> { this@App }

        import(rootComponentModule)
    }
}