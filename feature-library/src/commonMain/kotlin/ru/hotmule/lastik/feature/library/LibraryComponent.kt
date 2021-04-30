package ru.hotmule.lastik.feature.library

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.scrobbles.ScrobblesComponent
import ru.hotmule.lastik.feature.shelf.ShelfComponent
import ru.hotmule.lastik.feature.top.TopComponent

interface LibraryComponent {

    sealed class Child {
        data class Scrobbles(val component: ScrobblesComponent) : Child()
        data class Artists(val component: TopComponent) : Child()
        data class Albums(val component: TopComponent) : Child()
        data class Tracks(val component: TopComponent) : Child()
        data class Profile(val component: ProfileComponent) : Child()
    }

    data class Model(
        val activeShelfIndex: Int = 0,
        val periodSelectable: Boolean = false,
        val periodsOpened: Boolean = false,
        val selectedPeriodIndex: Int = 0,
        val logOutAllowed: Boolean = false
    )

    val routerState: Value<RouterState<*, Child>>

    val model: Flow<Model>

    fun onShelfSelect(index: Int)

    fun onPeriodSelectOpen()

    fun onPeriodSelectClose()

    fun onPeriodSelected(index: Int)

    fun onLogOut()
}