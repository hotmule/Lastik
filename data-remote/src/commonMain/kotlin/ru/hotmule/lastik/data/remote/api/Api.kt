package ru.hotmule.lastik.data.remote.api

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import korlibs.crypto.md5

fun HttpRequestBuilder.api(
    params: Map<String, Any?>,
    secret: String? = null
) {

    val token = params["token"]
    val username = params["username"]
    val password = params["password"]

    val signature = if (secret != null && (token != null || (username != null && password != null))) {
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