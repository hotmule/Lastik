package ru.hotmule.lastik.feature.browser

import org.kodein.di.DI
import org.kodein.di.DIAware
import java.awt.Desktop
import java.net.URI

actual class WebBrowser actual constructor(override val di: DI): DIAware {

    actual fun open(url: String) {
        if (Desktop.isDesktopSupported() &&
            Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        ) {
            Desktop.getDesktop().browse(URI(url))
        }
    }
}