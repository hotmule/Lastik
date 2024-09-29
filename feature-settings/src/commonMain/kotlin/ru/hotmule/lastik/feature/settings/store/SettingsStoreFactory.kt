package ru.hotmule.lastik.feature.settings.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.hotmule.lastik.data.sdk.permission.PermissionManager
import ru.hotmule.lastik.data.sdk.packages.PackageManager
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.feature.settings.SettingsComponent
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

internal class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val permissionManager: PermissionManager,
    private val packageManager: PackageManager,
    private val prefsStore: PrefsStore
) {
    fun create(): SettingsStore = object : SettingsStore,
        Store<SettingsStore.Intent, SettingsStore.State, Nothing> by storeFactory.create(
            name = SettingsStore::class.simpleName,
            initialState = SettingsStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<SettingsStore.Intent, Unit, SettingsStore.State, SettingsStore.Result, Nothing>(
            AppCoroutineDispatcher.Main
        ) {
        override fun executeAction(action: Unit) {
            scope.launch {
                dispatch(SettingsStore.Result.Loading(true))

                val scrobbleAppPackageNames = prefsStore.getScrobbleApps()

                dispatch(
                    SettingsStore.Result.AppsUpdated(
                        packageManager.getApps().map {
                            SettingsComponent.Package(
                                name = it.name,
                                label = it.label,
                                icon = it.icon,
                                isEnabled = it.name in scrobbleAppPackageNames
                            )
                        }
                    )
                )

                dispatch(SettingsStore.Result.Loading(false))
            }
        }

        override fun executeIntent(intent: SettingsStore.Intent) {
            when (intent) {
                SettingsStore.Intent.RefreshIsNotificationsAccessGranted -> {
                    dispatch(
                        SettingsStore.Result.SetNotificationsAccessGranted(
                            isGranted = permissionManager.isNotificationsAccessGranted()
                        )
                    )
                }
                SettingsStore.Intent.RequestNotificationsAccess -> {
                    permissionManager.requestNotificationsAccess()
                }
                is SettingsStore.Intent.SaveApp -> {

                    val checkedApp = state().apps.find { it.name == intent.packageName }

                    prefsStore.saveScrobbleApp(
                        packageName = intent.packageName,
                        enabledNow = checkedApp?.isEnabled == true
                    )

                    dispatch(
                        SettingsStore.Result.AppsUpdated(
                            state().apps.apply {
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

    object ReducerImpl : Reducer<SettingsStore.State, SettingsStore.Result> {
        override fun SettingsStore.State.reduce(msg: SettingsStore.Result): SettingsStore.State =
            when (msg) {
                is SettingsStore.Result.SetNotificationsAccessGranted -> copy(
                    isNotificationsAccessGranted = msg.isGranted,
                )
                is SettingsStore.Result.Loading -> copy(isLoading = msg.isLoading)
                is SettingsStore.Result.AppsUpdated -> copy(
                    apps = msg.apps.sortedWith(
                        compareBy(
                            { !it.isEnabled },
                            { it.label }
                        )
                    )
                )
            }
    }
}
