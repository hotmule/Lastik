package ru.hotmule.lastik.feature.browser

import org.kodein.di.DI
import org.kodein.di.DIAware

actual class WebBrowser actual constructor(override val di: DI) : DIAware {

    actual fun open(url: String) {

    }
}
