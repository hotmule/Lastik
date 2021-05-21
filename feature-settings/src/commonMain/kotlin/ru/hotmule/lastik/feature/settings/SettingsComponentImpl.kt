package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.DIAware

internal class SettingsComponentImpl(
    override val di: DI,
    private val onBack: () -> Unit,
    private val componentContext: ComponentContext
) : SettingsComponent, DIAware, ComponentContext by componentContext {

    override fun onBackPressed() {
        onBack()
    }
}