package ru.hotmule.lastik.feature.menu

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance
import ru.hotmule.lastik.feature.menu.MenuComponent.*
import ru.hotmule.lastik.feature.menu.store.MenuStore.*
import ru.hotmule.lastik.feature.menu.store.MenuStoreFactory
import ru.hotmule.lastik.utils.getStore

internal class MenuComponentImpl(
    override val di: DI,
    private val output: (Output) -> Unit,
    private val componentContext: ComponentContext,
) : MenuComponent, DIAware, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        MenuStoreFactory(
            storeFactory = direct.instance(),
            prefs = direct.instance(),
        ).create()
    }

    override val model: Flow<Model> = store.states.map {
        Model(
            isMenuOpened = it.isMenuOpened,
            isLogOutShown = it.isLogOutShown
        )
    }

    override fun onMenu() {
        store.accept(Intent.ProvideMenu)
    }

    override fun onOpenSettings() {
        store.accept(Intent.ProvideMenu)
        output(Output.SettingsOpened)
    }

    override fun onLogOut() {
        store.accept(Intent.LogOut)
    }

    override fun onLogOutCancel() {
        store.accept(Intent.LogOutCancel)
    }

    override fun onLogOutConfirm() {
        store.accept(Intent.LogOutConfirm)
    }
}