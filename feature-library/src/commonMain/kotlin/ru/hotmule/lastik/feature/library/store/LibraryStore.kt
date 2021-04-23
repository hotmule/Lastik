package ru.hotmule.lastik.feature.library.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.hotmule.lastik.feature.library.store.LibraryStore.*

interface LibraryStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class ChangeShelf(val index: Int) : Intent()
        object OpenPeriods : Intent()
        object ClosePeriods : Intent()
        data class SavePeriod(val index: Int) : Intent()
        object LogOut : Intent()
    }

    sealed class Result {
        data class ShelfSelected(val index: Int, val periodIndex: Int?) : Result()
        object PeriodsOpened : Result()
        object PeriodsClosed : Result()
        data class PeriodSelected(val index: Int) : Result()
    }

    data class State(
        val activeShelfIndex: Int = 0,
        val periodSelectable: Boolean = false,
        val periodsOpened: Boolean = false,
        val selectedPeriodIndex: Int = 0,
        val logOutAllowed: Boolean = false
    )
}