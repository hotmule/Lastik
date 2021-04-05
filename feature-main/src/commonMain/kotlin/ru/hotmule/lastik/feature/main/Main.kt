package ru.hotmule.lastik.feature.main

import com.arkivanov.decompose.ComponentContext

interface Main {

    interface Dependencies {

    }
}

fun Main(componentContext: ComponentContext, dependencies: Main.Dependencies): Main = MainImpl(
    componentContext,
    dependencies
)