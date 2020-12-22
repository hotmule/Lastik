package ru.hotmule.lastik.screen

import android.os.Build
import android.webkit.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.delay
import ru.hotmule.lastik.R
import ru.hotmule.lastik.domain.AuthInteractor

data class AuthScreenState(
    var isLoading: Boolean = false,
    var errorReceived: String? = null,
    var signInDialogOpened: Boolean = false
)

@Composable
fun AuthScreen(
    interactor: AuthInteractor
) {

    var state by remember { mutableStateOf(AuthScreenState()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .width(100.dp)
                .height(100.dp),
            imageVector = Icons.Rounded.Album,
            colorFilter = ColorFilter.tint(Color.Red)
        )

        with(state) {

            if (signInDialogOpened) {
                SignInDialog(
                    interactor,
                    onDismissRequest = {
                        state = state.copy(
                            signInDialogOpened = false
                        )
                    },
                    onTokenReceived = {
                        state = state.copy(
                            signInDialogOpened = false,
                            isLoading = true
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(bottom = true)
                    .padding(bottom = 16.dp)
            ) {

                if (isLoading) {
                    CircularProgressIndicator()
                    LaunchedEffect(true) {
                        try {
                            interactor.getSessionKey()
                        } catch (e: Exception) {
                            state = state.copy(errorReceived = e.message)
                        }
                        state = state.copy(isLoading = false)
                    }
                } else {
                    Button(
                        onClick = { state = state.copy(signInDialogOpened = true) },
                        content = { Text(text = stringResource(R.string.sign_in)) }
                    )
                }
            }

            errorReceived?.let {

                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                        .navigationBarsPadding(bottom = true),
                    text = { Text(it) },
                )

                LaunchedEffect(true) {
                    delay(3000)
                    state = state.copy(errorReceived = null)
                }
            }
        }
    }
}

@Composable
fun SignInDialog(
    interactor: AuthInteractor,
    onDismissRequest: () -> Unit,
    onTokenReceived: () -> Unit,
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

                SignInBrowser(
                    interactor = interactor,
                    onDismissRequest = onDismissRequest,
                    onTokenReceived = onTokenReceived
                )

                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
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
    val isLoading: Boolean = true,
    val hasWebView: Boolean = true,
    val isErrorReceived: Boolean = false
)

@Composable
private fun SignInBrowser(
    interactor: AuthInteractor,
    onDismissRequest: () -> Unit,
    onTokenReceived: () -> Unit,
) {

    var state by remember { mutableStateOf(SignInBrowserState()) }

    val context = AmbientContext.current
    val webView = remember {

        WebView(context).apply {

            dropSavedData()
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    state = state.copy(isLoading = false)
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
                    request?.url?.toString().let { url ->
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
                    }
                    return true
                }
            }

            loadUrl(interactor.getAuthUrl())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.58f)
    ) {

        with(state) {

            if (hasWebView) {
                AndroidView({ webView })
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (isErrorReceived) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    text = stringResource(id = R.string.page_loading_error),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1
                )
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