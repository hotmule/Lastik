package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.map
import org.kodein.di.*
import ru.hotmule.lastik.feature.settings.SettingsComponent.*
import ru.hotmule.lastik.feature.settings.store.SettingsStore.*
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
            packageManager = instance(),
            prefsStore = instance()
        ).create()
    }

    override val model = store.states.map { state ->
        Model(
            isLoading = state.isLoading,
            apps = state.apps
        )
    }

    override fun onBackPressed() {
        onBack()
    }

    override fun onAppClick(packageName: String) {
        store.accept(Intent.SaveApp(packageName))
    }
}