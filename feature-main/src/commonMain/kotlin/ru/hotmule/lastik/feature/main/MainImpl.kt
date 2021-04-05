package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext

class MainImpl(
    componentContext: ComponentContext
): Main, ComponentContext by componentContext {

}