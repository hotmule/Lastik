package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext

class MainComponentImpl(
    context: ComponentContext
): MainComponent, ComponentContext by context {

}