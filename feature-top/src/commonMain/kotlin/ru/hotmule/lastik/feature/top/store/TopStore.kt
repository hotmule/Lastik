package ru.hotmule.lastik.feature.top.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.top.store.TopStore.*

internal interface TopStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object OpenPeriods : Intent()
        object ClosePeriods : Intent()
        data class SavePeriod(val index: Int) : Intent()
    }

    sealed class Result {
        object PeriodsOpened : Result()
        object PeriodsClosed : Result()
        data class PeriodSelected(val index: Int) : Result()
    }

    data class State(
        val periodIndex: Int = 0,
        val periodsOpened: Boolean = false
    )
}