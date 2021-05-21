package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hotmule.lastik.feature.menu.MenuComponent
import ru.hotmule.lastik.ui.compose.res.Res
import ru.hotmule.lastik.feature.user.UserComponent
import ru.hotmule.lastik.feature.user.UserComponent.*

@Composable
fun UserContent(
    component: UserComponent,
    topInset: Dp
) {
    val model by component.model.collectAsState(Model())

    Scaffold(
        topBar = {
            UserTopBar(
                topInset = topInset,
                username = model.info.username,
                onMenu = component.menuComponent::onMenu
            )
        },
        content = {
            UserBody(model, component)
        }
    )
}

@Composable
private fun UserTopBar(
    topInset: Dp,
    username: String,
    onMenu: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(Res.Dimen.barHeight + topInset),
        title = {
            Text(
                modifier = Modifier.padding(top = topInset),
                text = username
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(top = topInset),
                onClick = onMenu
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
private fun UserBody(
    model: Model,
    component: UserComponent
) {
    val menuModel by component.menuComponent.model.collectAsState(MenuComponent.Model())

    Box {

        Box(
            modifier = if (menuModel.isMenuOpened) Modifier.offset(x = (-250).dp) else Modifier
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
        }

        if (menuModel.isMenuOpened) {
            MenuContent(
                component = component.menuComponent,
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            )
        }

        if (menuModel.isLogOutShown) {
            AlertDialog(
                title = { Text(text = Res.String.logging_out) },
                text = { Text(text = Res.String.logging_out_confirmation) },
                onDismissRequest = component.menuComponent::onLogOutCancel,
                confirmButton = {
                    TextButton(onClick = component.menuComponent::onLogOutConfirm) {
                        Text(text = Res.String.confirm)
                    }
                },
                dismissButton = {
                    TextButton(onClick = component.menuComponent::onLogOutCancel) {
                        Text(text = Res.String.cancel)
                    }
                }
            )
        }
    }
}

@Composable
expect fun AlertDialog(
    title: @Composable (() -> Unit)?,
    text: @Composable (() -> Unit)?,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)?
)

@Composable
private fun UserInfo(
    profile: User,
    friends: List<User>,
    isMoreFriendsLoading: Boolean
) {
    Column {

        UserStatistic(
            imageUrl = profile.image,
            scrobblingSince = profile.scrobblingSince,
            playCount = profile.playCount
        )

        TitleText(
            title = Res.String.friends,
            modifier = Modifier.padding(start = 16.dp)
        )

        Friends(
            friends = friends,
            isMoreLoading = isMoreFriendsLoading
        )

        TitleText(
            title = Res.String.loved_tracks,
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
            title = Res.String.scrobbling_since,
            subtitle = scrobblingSince,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        Statistic(
            title = Res.String.scrobbles,
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
    Image(
        painter = remoteImagePainter(url),
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
    friends: List<User>,
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
    friend: User
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