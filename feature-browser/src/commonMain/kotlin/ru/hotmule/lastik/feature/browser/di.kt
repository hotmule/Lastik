package ru.hotmule.lastik.feature.browser

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val browserModule = DI.Module("browser") {
    bind<WebBrowser>() with singleton { WebBrowser(di) }
}