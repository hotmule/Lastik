package ru.hotmule.lastik.feature.menu

import kotlinx.coroutines.flow.Flow

interface MenuComponent {

    data class Model(
        val isMenuOpened: Boolean = false,
        val isLogOutShown: Boolean = false
    )

    val model: Flow<Model>

    fun onMenu()

    fun onOpenSettings()

    fun onLogOut()

    fun onLogOutConfirm()

    fun onLogOutCancel()
}