package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.main.MainComponent
import ru.hotmule.lastik.feature.top.TopComponent
import ru.hotmule.lastik.feature.profile.ProfileComponent

interface LibraryComponent {

    val nowPlayingComponent: NowPlayingComponent

    sealed class Child(val index: Int) {
        data class Scrobbles(val component: MainComponent) : Child(0)
        data class Artists(val component: TopComponent) : Child(1)
        data class Albums(val component: TopComponent) : Child(2)
        data class Tracks(val component: TopComponent) : Child(3)
        data class Profile(val component: ProfileComponent) : Child(4)
    }

    val stack: Value<ChildStack<*, Child>>

    val activeChildIndex: Value<Int>

    fun onShelfSelect(index: Int)
}
