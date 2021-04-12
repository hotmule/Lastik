package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext

interface MainComponent {

}

fun MainComponent(
    context: ComponentContext
): MainComponent = MainComponentImpl(
    context
)