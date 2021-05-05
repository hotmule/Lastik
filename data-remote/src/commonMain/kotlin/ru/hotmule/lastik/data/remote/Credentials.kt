package ru.hotmule.lastik.data.remote

expect class Credentials() {
    val apiKey: String
    val secret: String
}