package ru.hotmule.lastfmclient.screen

import android.compose.utils.statusBarsPadding
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.hotmule.lastfmclient.R
import ru.hotmule.lastfmclient.Sdk
import ru.hotmule.lastfmclient.domain.AuthInteractor
import ru.hotmule.lastfmclient.domain.UserInteractor

@Composable
fun Profile(
    sdk: Sdk
) {
    Stack(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val scroll = rememberScrollState(0f)
        Title(sdk.userInteractor, scroll.value)
        Body(sdk.authInteractor, scroll)
    }
}

@Composable
fun Title(interactor: UserInteractor, scroll: Float) {

}

@Composable
fun Body(interactor: AuthInteractor, scroll: ScrollState) {
    ScrollableColumn(
        scrollState = scroll,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { interactor.signOut() }
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.sign_out)
            )
        }
    }
}