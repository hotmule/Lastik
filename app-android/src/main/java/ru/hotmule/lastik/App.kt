package ru.hotmule.lastik

import android.app.Application
import android.content.Context
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindInstance
import ru.hotmule.lastik.feature.root.rootComponentModule

class App : Application(), DIAware {

    override val di by DI.lazy {

        import(rootComponentModule)

        bindInstance<Context> { this@App }
    }
}
