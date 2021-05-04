package ru.hotmule.lastik.feature.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

actual class WebBrowser actual constructor(override val di: DI): DIAware {

    private val context: Context by instance()

    actual fun open(url: String) {
        CustomTabsIntent.Builder()
            .build()
            .apply { intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            .launchUrl(context, Uri.parse(url))
    }
}