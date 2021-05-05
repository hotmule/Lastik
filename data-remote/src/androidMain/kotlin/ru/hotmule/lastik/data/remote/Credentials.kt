package ru.hotmule.lastik.data.remote

actual class Credentials {
    actual val apiKey: String = BuildConfig.API_KEY
    actual val secret: String = BuildConfig.SECRET
}