package ru.hotmule.lastik.feature.app

import org.kodein.di.DI
import org.kodein.di.bindSingleton

val scrobblerComponentModule = DI.Module("appComponent") {

    bindSingleton { ScrobblerComponentImpl(directDI) }
}