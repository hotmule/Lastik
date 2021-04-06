package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext

interface MainComponent {

    interface Dependencies {

    }
}

fun MainComponent(
    context: ComponentContext,
    dependencies: MainComponent.Dependencies
): MainComponent = MainComponentImpl(
    context,
    dependencies
)