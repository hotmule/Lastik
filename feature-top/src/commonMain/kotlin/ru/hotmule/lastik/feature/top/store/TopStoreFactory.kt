package ru.hotmule.lastik.feature.top.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.flow.collect
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.feature.top.store.TopStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class TopStoreFactory(
    private val storeFactory: StoreFactory,
    private val prefsStore: PrefsStore,
    private val index: Int
) {
    fun create(): TopStore =
        object : TopStore, Store<Intent, State, Nothing> by storeFactory.create(
            TopStore::class.simpleName,
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeAction(
            action: Unit,
            getState: () -> State
        ) {
            prefsStore.getTopPeriodAsFlow(index).collect {
                dispatch(Result.PeriodSelected(it))
            }
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.OpenPeriods -> dispatch(Result.PeriodsOpened)
                Intent.ClosePeriods -> dispatch(Result.PeriodsClosed)
                is Intent.SavePeriod -> savePeriod(index, intent.index)
            }
        }

        private fun savePeriod(shelfIndex: Int, periodIndex: Int) {
            with (prefsStore) {
                when (shelfIndex) {
                    1 -> artistsPeriod = periodIndex
                    2 -> albumsPeriod = periodIndex
                    3 -> tracksPeriod = periodIndex
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {

        override fun State.reduce(result: Result): State = when (result) {
            Result.PeriodsOpened -> copy(periodsOpened = true)
            Result.PeriodsClosed -> copy(periodsOpened = false)
            is Result.PeriodSelected -> copy(
                periodIndex = result.index,
                periodsOpened = false
            )
        }
    }
}