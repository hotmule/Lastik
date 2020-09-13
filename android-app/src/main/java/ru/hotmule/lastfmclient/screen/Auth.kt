package ru.hotmule.lastfmclient.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen() {
    Scaffold(
        bodyContent = {
            Stack(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Image(
                    modifier = Modifier
                        .gravity(Alignment.Center)
                        .width(100.dp)
                        .height(100.dp),
                    asset = Icons.Rounded.Album,
                    colorFilter = ColorFilter.tint(Color.Red)
                )

                Button(
                    modifier = Modifier
                        .gravity(Alignment.BottomCenter)
                        .padding(bottom = 64.dp),
                    onClick = { }
                ) {
                    Text(text = "Авторизоваться")
                }
            }
        }
    )
}