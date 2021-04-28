package ru.hotmule.lastik.ui.compose

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.runtime.Composable

@Composable
actual fun getScreenWidth() : Int = LocalAppWindow.current.width