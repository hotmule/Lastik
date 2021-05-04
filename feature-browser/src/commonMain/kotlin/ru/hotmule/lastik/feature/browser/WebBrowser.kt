package ru.hotmule.lastik.feature.browser

import org.kodein.di.DI
import org.kodein.di.DIAware

expect class WebBrowser(di: DI): DIAware {
    fun open(url: String)
}