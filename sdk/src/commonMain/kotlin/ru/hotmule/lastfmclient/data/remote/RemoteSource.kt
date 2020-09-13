package ru.hotmule.lastfmclient.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom

class RemoteSource(
    private val endpoint: String,
    private val client: HttpClient
) {

    private fun HttpRequestBuilder.apiUrl(
        path: String,
        requestBody: Any? = null,
        token: String? = null
    ) {
        url {
            takeFrom(endpoint)
            encodedPath = "/api/$path"
            token?.let {
                header("access-token", token)
            }
        }
        requestBody?.let {
            contentType(ContentType.Application.Json)
            body = requestBody
        }
    }
}