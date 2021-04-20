package ru.hotmule.lastik.ui.compose

import android.os.Build
import android.webkit.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
private fun SignInBrowser(
    onDismissRequest: () -> Unit,
    onTokenReceived: () -> Unit,
) {

    //var state by remember { mutableStateOf(SignInBrowserState()) }

    val context = LocalContext.current
    val webView = remember {

        WebView(context).apply {

            dropSavedData()
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    //state = state.copy(isLoading = false)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    /*
                    state = state.copy(
                        isLoading = false,
                        hasWebView = false,
                        isErrorReceived = true
                    )
                    */
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.url?.toString().let { url ->
                        /*
                        if (interactor.urlContainsToken(url)) {
                            state = state.copy(
                                isLoading = true,
                                hasWebView = false
                            )
                            onTokenReceived.invoke()
                            onDismissRequest.invoke()
                            return false
                        } else
                            view?.loadUrl(url)

                        */
                    }
                    return true
                }
            }

            //loadUrl(interactor.getAuthUrl())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.58f)
    ) {

        /*
        with(state) {

            if (hasWebView) {
                AndroidView({ webView })
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = androidx.compose.ui.Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            }

            if (isErrorReceived) {
                Text(
                    modifier = androidx.compose.ui.Modifier
                        .align(androidx.compose.ui.Alignment.Center)
                        .padding(16.dp),
                    text = stringResource(id = R.string.page_loading_error),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = androidx.compose.material.MaterialTheme.typography.body1
                )
            }
        }
        */
    }
}

private fun WebView.dropSavedData() {

    clearCache(true)
    clearHistory()
    settings.apply {
        saveFormData = false
        savePassword = false
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    } else {
        with(CookieSyncManager.createInstance(context)) {
            startSync()
            CookieManager.getInstance().apply {
                removeAllCookie()
                removeSessionCookie()
            }
            stopSync()
            sync()
        }
    }
}