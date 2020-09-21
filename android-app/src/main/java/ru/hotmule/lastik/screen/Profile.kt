package ru.hotmule.lastik.screen

import android.compose.utils.statusBarsHeightPlus
import android.compose.utils.statusBarsPadding
import androidx.compose.foundation.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.hotmule.lastik.Sdk

@Composable
fun ProfileScreen(
    sdk: Sdk,
    toBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .statusBarsHeightPlus(BarsHeight),
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.statusBarsPadding(),
                        onClick = { toBack() },
                        icon = { Icon(asset = Icons.Rounded.ArrowBack) }
                    )
                },
                title = {
                    Text(
                        modifier = Modifier.statusBarsPadding(),
                        text = sdk.profileInteractor.getName()
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.statusBarsPadding(),
                        onClick = { sdk.authInteractor.signOut() },
                        icon = { Icon(asset = Icons.Rounded.ExitToApp) }
                    )
                }
            )
        },
        bodyContent = {

        }
    )
}