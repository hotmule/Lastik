package ru.hotmule.lastik.feature.settings

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.utils.Bitmap

interface SettingsComponent {

    data class Package(
        val name: String = "",
        val label: String = "",
        val bitmap: Bitmap? = null,
        var isEnabled: Boolean = false
    )

    data class Model(
        val isLoading: Boolean = false,
        val apps: List<Package> = emptyList()
    )

    val model: Flow<Model>

    fun onBackPressed()

    fun onAppClick(packageName: String)
}