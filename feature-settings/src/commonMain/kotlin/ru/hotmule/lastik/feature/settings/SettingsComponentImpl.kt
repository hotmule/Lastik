package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.map
import org.kodein.di.*
import ru.hotmule.lastik.feature.settings.SettingsComponent.*
import ru.hotmule.lastik.feature.settings.store.SettingsStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class SettingsComponentImpl(
    override val directDI: DirectDI,
    private val onBack: () -> Unit,
    private val componentContext: ComponentContext
) : SettingsComponent, DirectDIAware, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        SettingsStoreFactory(
            storeFactory = instance(),
            prefs = instance()
        ).create()
    }

    override val model = store.states.map {
        Model(
            apps = it.apps
        )
    }

    override fun onBackPressed() {
        onBack()
    }
}