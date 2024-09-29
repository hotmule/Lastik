package ru.hotmule.lastik.feature.settings

import coil3.Image
import kotlinx.coroutines.flow.Flow

interface SettingsComponent {

    data class Package(
        val name: String = "",
        val label: String = "",
        val icon: Image? = null,
        var isEnabled: Boolean = false
    )

    data class Model(
        val isNotificationsAccessGranted: Boolean = true,
        val isLoading: Boolean = false,
        val apps: List<Package> = emptyList()
    )

    val model: Flow<Model>

    fun onBackPressed()

    fun onNotificationsAccessRequest()

    fun onAppClick(packageName: String)
}
