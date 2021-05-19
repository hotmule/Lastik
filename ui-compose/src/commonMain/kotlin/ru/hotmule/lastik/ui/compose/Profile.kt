package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun ProfileContent(
    component: ProfileComponent,
    topInset: Dp
) {

    val model by component.model.collectAsState(Model())
    val activeChild by component.activeChild.asState()

    Scaffold(
        topBar = {
            when (activeChild) {
                is Child.Shelf -> {
                    ProfileTopBar(
                        topInset = topInset,
                        username = model.profile.username,
                        onMenu = component::onMenu
                    )
                }
                is Child.Settings -> {
                    SettingsTopBar(
                        topInset = topInset,
                        onPop = component::onPop
                    )
                }
            }
        },
        content = {
            ProfileBody(model, component)
        }
    )
}

@Composable
private fun ProfileTopBar(
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
            TopBarButton(
                topInset = topInset,
                onClick = onMenu,
                icon = Icons.Rounded.Menu,
                contentDescription = "Menu"
            )
        }
    )
}

@Composable
private fun SettingsTopBar(
    topInset: Dp,
    onPop: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(Res.Dimen.barHeight + topInset),
        title = {
            Text(
                modifier = Modifier.padding(top = topInset),
                text = Res.Array.profileMenu.first()
            )
        },
        navigationIcon = {
            TopBarButton(
                topInset = topInset,
                onClick = onPop,
                icon = Icons.Rounded.ArrowBack,
                contentDescription = "Pop"
            )
        }
    )
}

@Composable
private fun TopBarButton(
    topInset: Dp,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.padding(top = topInset),
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Composable
private fun ProfileBody(
    model: Model,
    component: ProfileComponent
) {
    Box {

        Box(
            modifier = if (model.menuOpened) Modifier.offset(x = (-250).dp) else Modifier
        ) {

            Children(component.routerState) {
                it.instance.let { child ->
                    when (child) {
                        is Child.Shelf -> ShelfContent(
                            component = child.component,
                            header = {
                                ProfileInfo(
                                    profile = model.profile,
                                    friends = model.friends,
                                    isMoreFriendsLoading = model.isMoreFriendsLoading
                                )
                            },
                            onRefreshHeader = component::onRefresh
                        )
                        is Child.Settings -> SettingsContent(child.component)
                    }
                }
            }
        }

        if (model.menuOpened) {

            Column(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            ) {

                Res.Array.profileMenu.forEachIndexed { index, item ->

                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colors.onBackground
                        ),
                        onClick = {
                            when (index) {
                                0 -> component.onOpenSettings()
                                else -> component.onLogOut()
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    ) {
                        Icon(
                            contentDescription = "menu item $index",
                            imageVector = when (index) {
                                0 -> Icons.Rounded.Tune
                                else -> Icons.Rounded.Logout
                            }
                        )
                        Text(
                            text = item,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f)
                        )
                    }
                }
            }
        }

        if (model.logOutConfirmationShown) {

            AlertDialog(
                title = { Text(text = Res.String.logging_out)  },
                text = { Text(text = Res.String.logging_out_confirmation) },
                onDismissRequest = component::onLogOutCancel,
                confirmButton = {
                    TextButton(onClick = component::onLogOutConfirm) {
                        Text(text = Res.String.confirm)
                    }
                },
                dismissButton = {
                    TextButton(onClick = component::onLogOutCancel) {
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
private fun ProfileInfo(
    profile: User,
    friends: List<User>,
    isMoreFriendsLoading: Boolean
) {
    Column {

        ProfileStatistic(
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
private fun ProfileStatistic(
    imageUrl: String,
    scrobblingSince: String,
    playCount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp)
    ) {

        ProfileImage(
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
private fun ProfileImage(
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

        ProfileImage(
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