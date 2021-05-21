package ru.hotmule.lastik.feature.settings

import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import org.kodein.di.DIAware
import ru.hotmule.lastik.feature.settings.SettingsComponent.*

internal class SettingsComponentImpl(
    override val di: DI,
    private val output: (Output) -> Unit,
    private val componentContext: ComponentContext
) : SettingsComponent, DIAware, ComponentContext by componentContext {

    override fun onBackPressed() {
        output(Output.BackPressed)
    }
}