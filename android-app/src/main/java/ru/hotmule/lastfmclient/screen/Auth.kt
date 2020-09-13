package ru.hotmule.lastfmclient.screen

import android.webkit.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import ru.hotmule.lastfmclient.BuildConfig
import ru.hotmule.lastfmclient.R

@Composable
fun AuthScreen() {

    var openDialog by mutableStateOf(false)

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
            onClick = { openDialog = true }
        ) {
            Text(text = stringResource(R.string.sign_in))
        }

        if (openDialog) AuthDialog(onDismissRequest = { openDialog = false })
    }
}

@Composable
fun AuthDialog(
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

                Stack(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.58f)
                ) {

                    var isLoading by remember { mutableStateOf(true) }
                    var errorReceived by remember { mutableStateOf(false) }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.gravity(Alignment.Center)
                        )
                    }

                    if (!errorReceived) {
                        AndroidView(
                            viewBlock = ::WebView,
                        ) {
                            it.apply {
                                webViewClient = object : WebViewClient() {

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        isLoading = false
                                    }

                                    override fun onReceivedError(
                                        view: WebView?,
                                        request: WebResourceRequest?,
                                        error: WebResourceError?
                                    ) {
                                        errorReceived = true
                                    }
                                }
                                loadUrl("https://www.last.fm/api/auth?api_key=${BuildConfig.API_KEY}/")
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .gravity(Alignment.Center)
                                .padding(16.dp),
                            text = stringResource(id = R.string.page_loading_error),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }

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