package ru.hotmule.lastfmclient

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.takeFrom

private val client = HttpClient()

suspend fun getScrobbles() = client.get<Scrobbles> {
    url {
        takeFrom("http://ws.audioscrobbler.com/2.0/?api_key=83e8a034819711968af5e5d5cf7cc4bd&format=json")
        parameter("method", "user.getrecenttracks")
        parameter("user", "hotmu1e")
    }
}