package com.jetbrains.handson.mpp.mobile

expect fun platformName(): String

fun createApplicationScreenMessage() = "Kotlin Rocks on ${platformName()}"