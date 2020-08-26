package ru.hotmule.lastfmclient

import androidx.compose.runtime.Composable

@Composable
fun LastFmClientApp() {
    AppTheme {
        AppContent()
    }
}

@Composable
fun AppContent() {
    LibraryScreen()
}