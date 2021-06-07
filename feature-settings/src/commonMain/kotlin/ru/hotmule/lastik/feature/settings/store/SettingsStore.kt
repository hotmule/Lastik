package ru.hotmule.lastik.feature.settings.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.settings.store.SettingsStore.*

internal interface SettingsStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class CheckApp(val id: String): Intent()
    }

    sealed class Result {
        data class AppsReceived(val apps: List<String>) : Result()
        data class AppChecked(val id: String): Result()
    }

    data class State(
        val apps: List<String> = emptyList()
    )
}