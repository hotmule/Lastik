package ru.hotmule.lastik.feature.settings

interface SettingsComponent {

    sealed class Output {
        object BackPressed: Output()
    }

    fun onBackPressed()
}