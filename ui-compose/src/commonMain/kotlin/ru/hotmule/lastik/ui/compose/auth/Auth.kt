package ru.hotmule.lastik.ui.compose.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import ru.hotmule.lastik.feature.auth.AuthComponent
import ru.hotmule.lastik.ui.compose.Resource

@Composable
fun AuthContent(
    component: AuthComponent
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(Resource.String.auth)
                }
            )
        },
        content = {
            AuthBody(
                component = component,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun AuthBody(
    component: AuthComponent,
    modifier: Modifier = Modifier
) {

    val state by component.state.asState()

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            label = { Text(Resource.String.login) },
            value = state.login,
            onValueChange = {
                component.onLoginChanged(it)
            },
            modifier = modifier
        )
        OutlinedTextField(
            label = { Text(Resource.String.password) },
            value = state.password,
            onValueChange = {
                component.onPasswordChanged(it)
            },
            modifier = modifier
        )
        Button(
            onClick = {
                component.onSignIn()
            },
            modifier = modifier
                .padding(top = 8.dp)
                .height(56.dp)
        ) {
            Text(Resource.String.sign_in)
        }
        TextButton(
            onClick = {

            },
            modifier = modifier.padding(top = 8.dp).height(56.dp)
        ) {
            Text(Resource.String.sign_in_with_last_fm)
        }
    }
}