package ru.hotmule.lastik.feature.root

import com.arkivanov.decompose.ComponentContext

class LastikRootImpl(
    componentContext: ComponentContext
) : LastikRoot, ComponentContext by componentContext {

}