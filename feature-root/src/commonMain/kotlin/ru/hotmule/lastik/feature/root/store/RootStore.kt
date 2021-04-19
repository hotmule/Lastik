package ru.hotmule.lastik.feature.root.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.root.store.RootStore.*

interface RootStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class TokenUrlReceived(val url: String): Intent()
    }

    object State
}