package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import ru.hotmule.lastik.feature.main.MainComponent

@Composable
fun MainContent(component: MainComponent) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Main",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}