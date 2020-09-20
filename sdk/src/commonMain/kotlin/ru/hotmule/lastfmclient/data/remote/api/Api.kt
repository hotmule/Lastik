package ru.hotmule.lastfmclient.data.remote.api

import com.soywiz.krypto.md5
import io.ktor.client.request.*
import io.ktor.http.takeFrom
import io.ktor.utils.io.core.*

fun HttpRequestBuilder.api(
    section: String,
    method: String,
    apiKey: String,
    token: String? = null,
    secret: String? = null,
    sessionKey: String? = null
) {

    val signature = if (token != null && secret != null) {
        "api_key${apiKey}method${section}.${method}token$token$secret"
            .toByteArray()
            .md5()
            .hex
    } else
        null

    url {
        takeFrom("http://ws.audioscrobbler.com")
        encodedPath = "/2.0/"

        parameter("format", "json")
        parameter("method", "$section.$method")
        parameter("api_key", apiKey)

        token?.let { parameter("token", it) }
        signature?.let { parameter("api_sig", it) }
        sessionKey?.let { parameter("sk", it) }
    }
}