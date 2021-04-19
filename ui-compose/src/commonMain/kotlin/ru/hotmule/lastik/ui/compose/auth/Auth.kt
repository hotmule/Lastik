package ru.hotmule.lastik.ui.compose.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.feature.auth.AuthComponentImpl
import ru.hotmule.lastik.feature.auth.store.AuthStore
import ru.hotmule.lastik.ui.compose.Res

@Composable
fun AuthContent(
    component: AuthComponentImpl,
    topInset: Dp,
    bottomInset: Dp
) {
    Scaffold(
        topBar = {
            AuthBar(
                topInset = topInset
            )
        },
        content = {
            AuthBody(
                component = component
            )
        },
        snackbarHost = { hostState ->
            AuthMessage(
                hostState = hostState,
                component = component,
                bottomInset = bottomInset
            )
        }
    )
}

@Composable
private fun AuthBar(
    topInset: Dp
) {
    TopAppBar(
        title = {
            Text(
                text = Res.String.auth,
                modifier = Modifier.padding(top = topInset)
            )
        },
        modifier = Modifier.height(Res.Dimen.appBarHeight + topInset)
    )
}

@Composable
private fun AuthBody(
    component: AuthComponentImpl
) {

    val model by component.model.collectAsState(AuthComponent.Model())

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            label = { Text(Res.String.login) },
            value = model.login,
            onValueChange = {
                component.onLoginChanged(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            label = { Text(Res.String.password) },
            value = model.password,
            onValueChange = {
                component.onPasswordChanged(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                component.onSignIn()
            },
            enabled = !model.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(56.dp)
        ) {
            Text(Res.String.sign_in)
        }

        TextButton(
            onClick = {
                component.onSignInWithLastFm()
            },
            enabled = !model.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(56.dp)
        ) {
            Text(Res.String.sign_in_with_last_fm)
        }

        if (model.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun AuthMessage(
    component: AuthComponentImpl,
    hostState: SnackbarHostState,
    bottomInset: Dp
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = {
            Snackbar(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(hostState.currentSnackbarData?.message ?: "")
            }
        },
        modifier = Modifier.padding(bottom = bottomInset)
    )

    LaunchedEffect("showError") {
        component.label.collect { label ->
            when (label) {
                is AuthStore.Label.MessageReceived -> {
                    hostState.showSnackbar(label.message)
                }
            }
        }
    }
}