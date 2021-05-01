package ru.hotmule.lastik.utils

expect object Formatter {
    fun numberToCommasString(number: Long?) : String
    fun utsDateToString(uts: Long?, pattern: String) : String
}