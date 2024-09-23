package ru.hotmule.lastik.feature.top.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
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

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override fun executeAction(action: Unit) {
            scope.launch {
                prefsStore.getTopPeriodAsFlow(index).collect {
                    dispatch(Result.PeriodSelected(it))
                }
            }
        }

        override fun executeIntent(intent: Intent) {
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

        override fun State.reduce(msg: Result): State = when (msg) {
            Result.PeriodsOpened -> copy(periodsOpened = true)
            Result.PeriodsClosed -> copy(periodsOpened = false)
            is Result.PeriodSelected -> copy(
                periodIndex = msg.index,
                periodsOpened = false
            )
        }
    }
}