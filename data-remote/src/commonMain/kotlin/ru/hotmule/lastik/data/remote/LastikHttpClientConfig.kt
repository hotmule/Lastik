package ru.hotmule.lastik.data.remote

import io.ktor.client.engine.*

expect class LastikHttpClientConfig() {
    val apiKey: String
    val secret: String
    val userAgent: String?
    val loggingEnabled: Boolean
    val engine: HttpClientEngine
}