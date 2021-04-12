package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.ValueObserver
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.rx.Disposable
import ru.hotmule.lastik.feature.auth.store.AuthStore
import ru.hotmule.lastik.feature.auth.store.AuthStore.*
import ru.hotmule.lastik.feature.auth.store.AuthStoreFactory
import ru.hotmule.lastik.feature.auth.store.AuthStoreFactory.*

class AuthComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        AuthStoreFactory(
            storeFactory = storeFactory
        ).create()
    }

    val state: Value<State> = store.asValue()

    fun onLoginChanged(login: String) {
        store.accept(Intent.ChangeLogin(login))
    }

    fun onPasswordChanged(password: String) {
        store.accept(Intent.ChangePassword(password))
    }

    fun onSignIn() {
        store.accept(Intent.SignIn)
    }
}

fun <T : Store<*, *, *>> InstanceKeeper.getStore(key: Any, factory: () -> T): T =
    getOrCreate(key) { StoreHolder(factory()) }
        .store

inline fun <reified T : Store<*, *, *>> InstanceKeeper.getStore(noinline factory: () -> T): T =
    getStore(T::class, factory)

private class StoreHolder<T : Store<*, *, *>>(
    val store: T
) : InstanceKeeper.Instance {
    override fun onDestroy() {
        store.dispose()
    }
}

fun <T : Any> Store<*, T, *>.asValue(): Value<T> = object : Value<T>() {

    private var disposables = emptyMap<ValueObserver<T>, Disposable>()

    override val value: T get() = state

    override fun subscribe(observer: ValueObserver<T>) {
        val disposable = states(com.arkivanov.mvikotlin.rx.observer(onNext = observer))
        this.disposables += observer to disposable
    }

    override fun unsubscribe(observer: ValueObserver<T>) {
        val disposable = disposables[observer] ?: return
        this.disposables -= observer
        disposable.dispose()
    }
}