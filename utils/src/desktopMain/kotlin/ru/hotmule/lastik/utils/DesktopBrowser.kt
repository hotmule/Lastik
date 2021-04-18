package ru.hotmule.lastik.utils

import java.awt.Desktop
import java.net.URI

class DesktopBrowser : ru.hotmule.lastik.utils.WebBrowser {
    override fun open(url: String) {
        if (Desktop.isDesktopSupported() &&
            Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        ) {
            Desktop.getDesktop().browse(URI(url))
        }
    }
}