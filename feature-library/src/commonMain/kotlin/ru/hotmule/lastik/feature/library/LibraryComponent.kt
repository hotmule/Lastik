package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.top.TopComponent

interface LibraryComponent {

    sealed class Child(val index: Int) {
        data class Scrobbles(val component: ScrobblesComponent) : Child(0)
        data class Artists(val component: TopComponent) : Child(1)
        data class Albums(val component: TopComponent) : Child(2)
        data class Tracks(val component: TopComponent) : Child(3)
        data class Profile(val component: ProfileComponent) : Child(4)
    }

    val routerState: Value<RouterState<*, Child>>

    val activeChildIndex: Value<Int>

    fun onShelfSelect(index: Int)
}