package ru.hotmule.lastik.utils

import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

val Lifecycle.componentCoroutineScope: CoroutineScope
    get() = with(CoroutineScope(AppCoroutineDispatcher.IO)) {
        doOnDestroy { cancel() }
        this
    }