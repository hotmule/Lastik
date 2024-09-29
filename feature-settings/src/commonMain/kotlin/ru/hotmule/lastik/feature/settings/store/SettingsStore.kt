package ru.hotmule.lastik.feature.settings.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.settings.SettingsComponent

internal interface SettingsStore : Store<SettingsStore.Intent, SettingsStore.State, Nothing> {

    sealed class Intent {
        data object RefreshIsNotificationsAccessGranted : Intent()
        data object RequestNotificationsAccess: Intent()
        data class SaveApp(val packageName: String): Intent()
    }

    sealed class Result {
        data class SetNotificationsAccessGranted(val isGranted: Boolean) : Result()
        data class Loading(val isLoading: Boolean) : Result()
        data class AppsUpdated(val apps: List<SettingsComponent.Package>) : Result()
    }

    data class State(
        val isNotificationsAccessGranted: Boolean = true,
        val isLoading: Boolean = false,
        val apps: List<SettingsComponent.Package> = emptyList()
    )
}
