package ru.hotmule.lastfmclient

import androidx.compose.Composable

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