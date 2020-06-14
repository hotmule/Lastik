package ru.hotmule.lastfmclient

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.foundation.Text
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar

@Composable
fun MainScreen() {

    AppTheme {

        val currentPage = state { 1 }

        Scaffold(
            topAppBar = {
                TopAppBar {

                }
            },
            bodyContent = {
                Text(text = "Page ${currentPage.value} selected")
            },
            bottomAppBar = {
                BottomNavigation {
                    BottomNavigationItem(
                        icon = { },
                        selected = false,
                        text = { Text("First") },
                        onSelected = { currentPage.value = 1 }
                    )
                    BottomNavigationItem(
                        icon = { },
                        selected = false,
                        text = { Text("Second") },
                        onSelected = { currentPage.value = 2 }
                    )
                    BottomNavigationItem(
                        icon = { },
                        selected = false,
                        text = { Text("Third") },
                        onSelected = { currentPage.value = 3 }
                    )
                }
            }
        )
    }
}