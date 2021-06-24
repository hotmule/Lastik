package ru.hotmule.lastik.feature.settings.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.hotmule.lastik.data.sdk.packages.PackageManager
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.settings.SettingsComponent.*
import ru.hotmule.lastik.feature.settings.store.SettingsStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val packageManager: PackageManager,
    private val prefsStore: PrefsStore
) {
    fun create(): SettingsStore = object : SettingsStore,
        Store<Intent, State, Nothing> by storeFactory.create(
            name = SettingsStore::class.simpleName,
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeAction(action: Unit, getState: () -> State) {

            dispatch(Result.Loading(true))

            val scrobbleAppPackageNames = prefsStore.getScrobbleApps()

            dispatch(
                Result.AppsUpdated(
                    packageManager.getApps().map {
                        Package(
                            name = it.name,
                            label = it.label,
                            bitmap = it.bitmap,
                            isEnabled = it.name in scrobbleAppPackageNames
                        )
                    }
                )
            )

            dispatch(Result.Loading(false))
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SaveApp -> {

                    val checkedApp = getState().apps.find { it.name == intent.packageName }

                    prefsStore.saveScrobbleApp(
                        packageName = intent.packageName,
                        enabledNow = checkedApp?.isEnabled == true
                    )

                    dispatch(
                        Result.AppsUpdated(
                            getState().apps.apply {
                                get(indexOf(checkedApp)).apply {
                                    isEnabled = !isEnabled
                                }
                            }
                        )
                    )
                }
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.Loading -> copy(isLoading = result.isLoading)
            is Result.AppsUpdated -> copy(
                apps = result.apps.sortedWith(
                    compareBy(
                        { !it.isEnabled },
                        { it.label }
                    )
                )
            )
        }
    }
}