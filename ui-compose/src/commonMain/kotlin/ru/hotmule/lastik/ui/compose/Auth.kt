package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.ui.compose.res.Res
import ru.hotmule.lastik.ui.compose.utils.navigationBarPadding
import ru.hotmule.lastik.ui.compose.utils.statusBarHeight
import ru.hotmule.lastik.ui.compose.utils.statusBarPadding

@Composable
fun AuthContent(
    component: AuthComponent,
) {
    Scaffold(
        topBar = {
            AuthBar()
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
            )
        }
    )
}

@Composable
private fun AuthBar() {
    TopAppBar(
        title = {
            Text(
                text = Res.String.auth,
                modifier = Modifier.statusBarPadding()
            )
        },
        modifier = Modifier.height(Res.Dimen.barHeight + WindowInsets.statusBarHeight)
    )
}

@Composable
private fun AuthBody(
    component: AuthComponent
) {

    val model by component.model.collectAsState(AuthComponent.Model())

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            label = { Text(Res.String.username) },
            value = model.username,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false
            ),
            onValueChange = {
                component.onLoginChanged(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            label = { Text(Res.String.password) },
            value = model.password,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (model.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        component.onPasswordVisibilityChanged()
                    }
                ) {
                    Icon(
                        imageVector = if (model.isPasswordVisible) {
                            Icons.Rounded.VisibilityOff
                        } else {
                            Icons.Rounded.Visibility
                        },
                        contentDescription = "passwordVisibility"
                    )
                }
            },
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
    component: AuthComponent,
    hostState: SnackbarHostState,
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
        modifier = Modifier.navigationBarPadding()
    )

    LaunchedEffect("showError") {
        component.events.collect { event ->
            when (event) {
                is AuthComponent.Event.MessageReceived -> {
                    hostState.showSnackbar(event.message ?: "")
                }
            }
        }
    }
}