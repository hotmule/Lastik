package ru.hotmule.lastik.data.sdk.packages

import coil3.Image

data class Package(
    val name: String,
    val label: String,
    val icon: Image,
)