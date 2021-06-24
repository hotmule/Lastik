package ru.hotmule.lastik.feature.settings.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.settings.SettingsComponent.*
import ru.hotmule.lastik.feature.settings.store.SettingsStore.*

internal interface SettingsStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class SaveApp(val packageName: String): Intent()
    }

    sealed class Result {
        data class Loading(val isLoading: Boolean) : Result()
        data class AppsUpdated(val apps: List<Package>) : Result()
    }

    data class State(
        val isLoading: Boolean = false,
        val apps: List<Package> = emptyList()
    )
}