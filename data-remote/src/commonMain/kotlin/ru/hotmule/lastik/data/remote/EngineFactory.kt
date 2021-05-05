package ru.hotmule.lastik.data.remote

import io.ktor.client.engine.*
import org.kodein.di.DI
import org.kodein.di.DIAware

expect class EngineFactory(di: DI): DIAware {

    fun create(): HttpClientEngine

    fun isLoggingEnabled(): Boolean
    fun getUserAgent(): String?
}