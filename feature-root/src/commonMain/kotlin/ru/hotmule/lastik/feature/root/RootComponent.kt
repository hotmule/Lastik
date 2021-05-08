package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.library.LibraryComponent

interface RootComponent {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Library(val component: LibraryComponent) : Child()
        data class Auth(val component: AuthComponent) : Child()
    }

    fun onUrlReceived(url: String?)

    fun onTrackDetected(isPlaying: Boolean?, artist: String?, track: String?, time: Long?)
}