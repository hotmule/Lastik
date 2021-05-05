package ru.hotmule.lastik

import android.app.Application
import android.content.Context
import org.kodein.di.*
import ru.hotmule.lastik.feature.root.rootComponentModule

class App : Application(), DIAware {

    override val di by DI.lazy {

        bindInstance<Context> { this@App }

        import(rootComponentModule)
    }
}