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
import lastik.ui_compose.generated.resources.Res
import lastik.ui_compose.generated.resources.authorization
import lastik.ui_compose.generated.resources.password
import lastik.ui_compose.generated.resources.sign_in
import lastik.ui_compose.generated.resources.sign_in_with_last_fm
import lastik.ui_compose.generated.resources.username
import org.jetbrains.compose.resources.stringResource
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.ui.compose.common.LastikTopAppBar

@Composable
fun AuthContent(
    component: AuthComponent,
) {
    Scaffold(
        topBar = {
            LastikTopAppBar(
                title = stringResource(Res.string.authorization),
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
            )
        }
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
            label = { Text(stringResource(Res.string.username)) },
            value = model.username,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = false
            ),
            onValueChange = {
                component.onLoginChanged(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            label = { Text(stringResource(Res.string.password)) },
            value = model.password,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = false,
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
            Text(stringResource(Res.string.sign_in))
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
            Text(stringResource(Res.string.sign_in_with_last_fm))
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
        modifier = Modifier.navigationBarsPadding()
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