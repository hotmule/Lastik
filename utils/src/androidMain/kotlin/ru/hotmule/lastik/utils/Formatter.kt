package ru.hotmule.lastik.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

actual object Formatter {

    actual fun numberToCommasString(number: Long?): String =
        DecimalFormat("#,###,###").format(number)

    actual fun utsDateToString(uts: Long?, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(uts?.times(1000) ?: 0))
}