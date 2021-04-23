package ru.hotmule.lastik.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

class AndroidBrowser(private val context: Context): WebBrowser {

    override fun open(url: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(url))
    }
}