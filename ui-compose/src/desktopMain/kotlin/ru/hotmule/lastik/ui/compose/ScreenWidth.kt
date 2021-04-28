package ru.hotmule.lastik.ui.compose

import androidx.compose.desktop.LocalAppWindow

actual fun getScreenWidth() : Int = LocalAppWindow.current.width