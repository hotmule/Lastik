package ru.hotmule.lastfmclient.screen

import android.os.Build
import android.webkit.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import ru.hotmule.lastfmclient.R
import ru.hotmule.lastfmclient.domain.AuthInteractor


@Composable
fun AuthScreen(
    interactor: AuthInteractor
) {

    var signInDialogOpened by remember { mutableStateOf(false) }

    Stack(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Image(
            modifier = Modifier
                .gravity(Alignment.Center)
                .width(100.dp)
                .height(100.dp),
            asset = Icons.Rounded.Album,
            colorFilter = ColorFilter.tint(Color.Red)
        )

        Button(
            modifier = Modifier
                .gravity(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            onClick = { signInDialogOpened = true }
        ) {
            Text(text = stringResource(R.string.sign_in))
        }

        if (signInDialogOpened) {
            SignInDialog(
                interactor,
                onDismissRequest = { signInDialogOpened = false }
            )
        }
    }
}

@Composable
fun SignInDialog(
    interactor: AuthInteractor,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = R.string.authorization),
                    style = MaterialTheme.typography.h6
                )

                SignInBrowser(interactor)

                TextButton(
                    modifier = Modifier
                        .gravity(Alignment.End)
                        .padding(8.dp),
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.close))
                }
            }
        }
    }
}

data class SignInBrowserState(
    val isLoading: Boolean = false,
    val hasWebView: Boolean = false,
    val isErrorReceived: Boolean = false
)

@Composable
private fun SignInBrowser(
    interactor: AuthInteractor,
) {
    Stack(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.58f)
    ) {

        var state by remember {
            mutableStateOf(
                SignInBrowserState(
                    isLoading = true,
                    hasWebView = true,
                )
            )
        }

        with (state) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.gravity(Alignment.Center)
                )
            }

            if (isErrorReceived) {
                Text(
                    modifier = Modifier
                        .gravity(Alignment.Center)
                        .padding(16.dp),
                    text = stringResource(id = R.string.page_loading_error),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1
                )
            }

            if (hasWebView) {
                AndroidView(
                    viewBlock = ::WebView,
                ) {
                    it.dropSavedData()
                    it.webViewClient = object : WebViewClient() {

                        override fun onPageFinished(view: WebView?, url: String?) {
                            state = state.copy(
                                isLoading = false
                            )
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            state = state.copy(
                                isLoading = false,
                                hasWebView = false,
                                isErrorReceived = true
                            )
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {

                            val url = request?.url.toString()

                            launchInComposition {
                                val containsToken = interactor.checkForToken(url)
                                if (containsToken) {
                                    state = state.copy(
                                        isLoading = true,
                                        hasWebView = false
                                    )
                                }
                            }

                            return true
                        }
                    }
                    it.loadUrl(interactor.getAuthUrl())
                }
            }
        }
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