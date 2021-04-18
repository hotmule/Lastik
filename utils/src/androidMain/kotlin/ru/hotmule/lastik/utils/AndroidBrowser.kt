package ru.hotmule.lastik.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser

class AndroidBrowser(private val context: Context): WebBrowser {
    override fun open(url: String) {
        with(context) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                ).putExtra(
                    Browser.EXTRA_APPLICATION_ID,
                    packageName
                ).addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )
        }
    }
}