package ru.hotmule.lastik.feature.settings

import kotlinx.coroutines.flow.Flow

interface SettingsComponent {

    data class Model(
        val apps: List<String> = emptyList()
    )

    val model: Flow<Model>

    fun onBackPressed()
}