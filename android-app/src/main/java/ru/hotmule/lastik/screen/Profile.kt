package ru.hotmule.lastik.screen

import android.compose.utils.statusBarsHeightPlus
import android.compose.utils.statusBarsPadding
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.launchInComposition
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.theme.barHeight
import ru.hotmule.lastik.R
import ru.hotmule.lastik.domain.ProfileInteractor
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.hotmule.lastik.components.ProfileImage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    sdk: Sdk,
    toBack: () -> Unit
) {

    val isUpdating = mutableStateOf(false)

    Scaffold(
        topBar = {
            ProfileTopBar(
                modifier = Modifier.statusBarsHeightPlus(barHeight),
                nickname = sdk.profileInteractor.getNickname(),
                isUpdating = isUpdating.value,
                onSignOut = sdk.authInteractor::signOut,
                onBack = toBack
            )
        },
        bodyContent = {
            ProfileBody(
                isUpdating = { isUpdating.value = it },
                interactor = sdk.profileInteractor
            )
        }
    )
}

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
    nickname: String,
    isUpdating: Boolean,
    onSignOut: () -> Unit,
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                modifier = Modifier.statusBarsPadding(),
                onClick = { onBack.invoke() },
                icon = { Icon(asset = Icons.Rounded.ArrowBack) }
            )
        },
        title = {
            Text(
                modifier = Modifier.statusBarsPadding(),
                text = if (isUpdating) stringResource(id = R.string.updating) else nickname
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.statusBarsPadding(),
                onClick = { onSignOut.invoke() },
                icon = { Icon(asset = Icons.Rounded.ExitToApp) }
            )
        }
    )
}

@Composable
fun ProfileBody(
    isUpdating: (Boolean) -> Unit,
    interactor: ProfileInteractor,
) {
    launchInComposition {
        isUpdating.invoke(true)
        try {
            interactor.refreshInfo()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isUpdating.invoke(false)
    }

    val info by interactor.observeInfo().collectAsState(initial = null)

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {

        val (image, realName, regDateTitle, regDate, playCountTitle, playCount) = createRefs()

        ProfileImage(
            url = info?.highResImage,
            modifier = Modifier
                .preferredHeight(82.dp)
                .preferredWidth(82.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 20.dp)
                }
        )

        info?.registerDate?.let {

            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(
                    text = stringResource(id = R.string.scrobbling_since),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .constrainAs(regDateTitle) {
                            start.linkTo(image.end, 16.dp)
                            end.linkTo(playCount.start)
                            top.linkTo(image.top)
                            bottom.linkTo(regDate.top)
                        }
                )
            }

            Text(
                text = with (SimpleDateFormat("d MMMM yyyy", Locale.getDefault())) {
                    format(Date(it * 1000))
                },
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .constrainAs(regDate) {
                        start.linkTo(regDateTitle.start)
                        end.linkTo(playCount.start)
                        top.linkTo(regDateTitle.bottom, 8.dp)
                        bottom.linkTo(image.bottom)
                    }
            )
        }

        info?.playCount?.let {

            ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                Text(
                    text = stringResource(id = R.string.scrobbles),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .constrainAs(playCountTitle) {
                            top.linkTo(image.top)
                            bottom.linkTo(playCount.top)
                            end.linkTo(parent.end, 16.dp)
                            start.linkTo(regDateTitle.end)
                        }
                )
            }

            Text(
                text = it.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .constrainAs(playCount) {
                        end.linkTo(playCountTitle.end)
                        start.linkTo(regDate.end)
                        top.linkTo(playCountTitle.bottom, 8.dp)
                        bottom.linkTo(image.bottom)
                    }
            )
        }
    }
}