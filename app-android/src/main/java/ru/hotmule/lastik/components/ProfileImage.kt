package ru.hotmule.lastik.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.google.accompanist.coil.CoilImage

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    url: String?
) {
    CoilImage(
        data = url ?: "",
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}