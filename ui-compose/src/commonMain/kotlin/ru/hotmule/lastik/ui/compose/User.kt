package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import lastik.ui_compose.generated.resources.Res
import lastik.ui_compose.generated.resources.cancel
import lastik.ui_compose.generated.resources.confirm
import lastik.ui_compose.generated.resources.friends
import lastik.ui_compose.generated.resources.logging_out
import lastik.ui_compose.generated.resources.logging_out_confirmation
import lastik.ui_compose.generated.resources.loved_tracks
import lastik.ui_compose.generated.resources.scrobbles
import lastik.ui_compose.generated.resources.scrobbling_since
import org.jetbrains.compose.resources.stringResource
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.ui.compose.common.LastikTopAppBar

@Composable
fun UserContent(
    component: ProfileComponent,
) {
    val model by component.model.collectAsState(ProfileComponent.Model())

    Scaffold(
        topBar = {
            UserTopBar(
                username = model.info.username,
                onLogOut = component::onLogOut,
            )
        },
        content = {
            UserBody(model, component)
        }
    )
}

@Composable
private fun UserTopBar(
    username: String,
    onLogOut: () -> Unit,
) {
    LastikTopAppBar(
        title = username,
        actions = {
            IconButton(
                modifier = Modifier.statusBarsPadding(),
                onClick = onLogOut,
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = "LogOut",
                )
            }
        }
    )
}

@Composable
private fun UserBody(
    model: ProfileComponent.Model,
    component: ProfileComponent
) {
    ShelfContent(
        component = component.lovedTracksComponent,
        header = {
            UserInfo(
                profile = model.info,
                friends = model.friends,
                isMoreFriendsLoading = model.isMoreFriendsLoading
            )
        },
        onRefreshHeader = component::onRefresh
    )

    if (model.isLogOutShown) {
        AlertDialog(
            title = { Text(text = stringResource(Res.string.logging_out)) },
            text = { Text(text = stringResource(Res.string.logging_out_confirmation)) },
            onDismissRequest = component::onLogOutCancel,
            confirmButton = {
                TextButton(onClick = component::onLogOutConfirm) {
                    Text(text = stringResource(Res.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = component::onLogOutCancel) {
                    Text(text = stringResource(Res.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun UserInfo(
    profile: ProfileComponent.Profile,
    friends: List<ProfileComponent.Profile>,
    isMoreFriendsLoading: Boolean
) {
    Column {

        UserStatistic(
            imageUrl = profile.image,
            scrobblingSince = profile.scrobblingSince,
            playCount = profile.playCount
        )

        TitleText(
            title = stringResource(Res.string.friends),
            modifier = Modifier.padding(start = 16.dp)
        )

        Friends(
            friends = friends,
            isMoreLoading = isMoreFriendsLoading
        )

        TitleText(
            title = stringResource(Res.string.loved_tracks),
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 4.dp)
        )
    }
}

@Composable
private fun UserStatistic(
    imageUrl: String,
    scrobblingSince: String,
    playCount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp)
    ) {

        UserImage(
            url = imageUrl,
            modifier = Modifier
                .padding(24.dp)
                .height(96.dp)
                .width(96.dp)
        )

        Statistic(
            title = stringResource(Res.string.scrobbling_since),
            subtitle = scrobblingSince,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        Statistic(
            title = stringResource(Res.string.scrobbles),
            subtitle = playCount,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
    }
}

@Composable
private fun UserImage(
    modifier: Modifier = Modifier,
    url: String
) {
    AsyncImage(
        model = url,
        contentDescription = "profileImage",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}

@Composable
private fun Statistic(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
) {
    Column(modifier) {

        TitleText(
            title = title,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = subtitle,
            fontSize = 20.sp,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )
    }
}

@Composable
private fun TitleText(
    modifier: Modifier = Modifier,
    title: String
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
private fun Friends(
    friends: List<ProfileComponent.Profile>,
    isMoreLoading: Boolean
) {
    LazyRow(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        itemsIndexed(friends) { index, friend ->
            Friend(
                friend = friend,
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = if (index == 0) 16.dp else 8.dp,
                    end = if (index == friends.lastIndex) 16.dp else 8.dp
                )
            )

            if (index == friends.lastIndex) {

            }
        }
    }
}

@Composable
private fun Friend(
    modifier: Modifier = Modifier,
    friend: ProfileComponent.Profile,
) {
    Column(
        modifier = modifier.width(72.dp)
    ) {

        UserImage(
            url = friend.image,
            modifier = Modifier
                .width(72.dp)
                .height(72.dp)
                .fillMaxWidth()
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = friend.username,
                maxLines = 1,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )
        }
    }
}
