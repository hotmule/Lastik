package ru.hotmule.lastik.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual object AppCoroutineDispatcher {
    actual val IO: CoroutineDispatcher = Dispatchers.IO
    actual val Main: CoroutineDispatcher = Dispatchers.Main
}
