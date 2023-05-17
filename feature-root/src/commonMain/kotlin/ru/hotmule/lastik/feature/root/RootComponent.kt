package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.library.LibraryComponent

interface RootComponent {

    sealed class Child {
        data class Library(val component: LibraryComponent) : Child()
        data class Auth(val component: AuthComponent) : Child()
    }

    val stack: Value<ChildStack<*, Child>>

    fun onUrlReceived(url: String?)
}