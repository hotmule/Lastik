package ru.hotmule.lastik.feature.root.store

import com.arkivanov.mvikotlin.core.store.Store

internal interface RootStore: Store<RootStore.Intent, Unit, Unit> {

    sealed class Intent {
        data class ProcessUrl(val url: String?): Intent()
    }
}