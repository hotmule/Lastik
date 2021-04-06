package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext

class MainComponentImpl(
    context: ComponentContext,
    dependencies: MainComponent.Dependencies
): MainComponent, ComponentContext by context {

}