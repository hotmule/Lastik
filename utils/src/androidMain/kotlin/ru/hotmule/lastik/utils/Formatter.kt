package ru.hotmule.lastik.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

actual object Formatter {

    actual fun numberToCommasString(number: Long?): String = if (number == null) "" else
        DecimalFormat("#,###,###").format(number)

    actual fun utsDateToString(uts: Long?, pattern: String): String = if (uts == null) "" else
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(uts * 1000))
}