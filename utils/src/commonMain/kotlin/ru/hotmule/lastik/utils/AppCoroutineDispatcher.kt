package ru.hotmule.lastik.utils

import kotlinx.coroutines.CoroutineDispatcher

expect object AppCoroutineDispatcher {
    val IO: CoroutineDispatcher
    val Main: CoroutineDispatcher
}