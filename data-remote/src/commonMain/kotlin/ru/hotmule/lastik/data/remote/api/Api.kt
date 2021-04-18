package ru.hotmule.lastik.data.remote.api

import com.soywiz.krypto.md5
import io.ktor.client.request.*
import io.ktor.http.takeFrom
import io.ktor.utils.io.core.*

fun HttpRequestBuilder.api(
    params: Map<String, Any?>,
    secret: String? = null
) {

    val signature = if (params["token"] != null && secret != null) {
        params
            .toList()
            .sortedBy { it.first }
            .joinToString(separator = "") { "${it.first}${it.second}" }
            .plus(secret)
            .toByteArray()
            .md5()
            .hex
    } else
        null

    url {

        takeFrom("http://ws.audioscrobbler.com")
        encodedPath = "/2.0/"

        parameter("format", "json")
        params.forEach { parameter(it.key, it.value) }
        signature?.let { parameter("api_sig", it) }
    }
}