package ru.hotmule.lastik.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

actual object Formatter {

    actual fun numberToCommasString(number: Long?): String = if (number != null)
        DecimalFormat("#,###,###").format(number)
    else
        "0"

    actual fun utsDateToString(uts: Long?, pattern: String): String = if (uts != null)
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(uts * 1000))
    else
        ""
}