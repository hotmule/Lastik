package ru.hotmule.lastfmclient

import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.core.view.updatePadding
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        MaterialTheme {
            Column {
                ItemsScreen()
            }
        }
    }

    @Composable
    fun ItemsScreen() {
        VerticalScroller(
            modifier = Modifier.padding(16.dp)
        ) {
            Items(
                listOf(
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android",
                    "Android"
                )
            )
        }
    }

    @Composable
    fun Items(item: List<String>) {
        Column {
            item.forEach {
                ListItem(
                    title = it
                )
            }
        }
    }

    @Composable
    fun ListItem(title: String) {
        Text(
            text = "Hello, $title!",
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 16.dp
            )
        )
        Divider()
    }

    private fun setEdgeToEdge() {
        window?.decorView?.apply {
            systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setOnApplyWindowInsetsListener { view, insets ->
                view.updatePadding(top = insets.stableInsetTop)
                insets
            }
        }
    }
}