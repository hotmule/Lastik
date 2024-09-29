package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.DirectDIAware
import org.kodein.di.instance
import ru.hotmule.lastik.feature.settings.store.SettingsStore
import ru.hotmule.lastik.feature.settings.store.SettingsStoreFactory

internal class SettingsComponentImpl(
    override val directDI: DirectDI,
    private val onBack: () -> Unit,
    private val componentContext: ComponentContext
) : SettingsComponent, DirectDIAware, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        SettingsStoreFactory(
            storeFactory = instance(),
            permissionManager = instance(),
            packageManager = instance(),
            prefsStore = instance()
        ).create()
    }

    override val model = store.states.map { state ->
        SettingsComponent.Model(
            isNotificationsAccessGranted = state.isNotificationsAccessGranted,
            isLoading = state.isLoading,
            apps = state.apps
        )
    }

    init {
        lifecycle.doOnResume {
            store.accept(SettingsStore.Intent.RefreshIsNotificationsAccessGranted)
        }
    }

    override fun onBackPressed() {
        onBack()
    }

    override fun onNotificationsAccessRequest() {
        store.accept(SettingsStore.Intent.RequestNotificationsAccess)
    }

    override fun onAppClick(packageName: String) {
        store.accept(SettingsStore.Intent.SaveApp(packageName))
    }
}
