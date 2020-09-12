package ru.hotmule.lastfmclient

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var isSessionActive by mutableStateOf(false)
        private set

    fun onAuth() {
        isSessionActive = true
    }
}