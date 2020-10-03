package ru.hotmule.lastik.utlis

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long.toCommasString(): String {
    with (DecimalFormat("#,###,###")) {
        return format(this@toCommasString)
    }
}

fun Long.toDateString(pattern: String): String {
    with (SimpleDateFormat(pattern, Locale.getDefault())) {
        return format(Date(this@toDateString * 1000))
    }
}