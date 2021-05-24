package ru.hotmule.lastik.feature.app

import org.kodein.di.DI
import org.kodein.di.bindSingleton

val nowPlayingComponentModule = DI.Module("nowPlayingComponent") {

    bindSingleton { NowPlayingComponentImpl(directDI) }
}