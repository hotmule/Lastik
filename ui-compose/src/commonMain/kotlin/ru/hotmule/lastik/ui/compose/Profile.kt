package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.ProfileComponent.*
import ru.hotmule.lastik.ui.compose.res.Res

@Composable
fun ProfileContent(
    component: ProfileComponent,
    bottomInset: Dp
) {
    Children(component.routerState) { child, _ ->
        when (child) {
            is Child.Shelf -> ShelfContent(child.component, bottomInset) { ProfileInfo(component) }
        }
    }
}

@Composable
private fun ProfileInfo(
    component: ProfileComponent
) {
    val model by component.model.collectAsState(Model())

    Column {

        ProfileStatistic(
            imageUrl = model.profile.image,
            scrobblingSince = model.profile.scrobblingSince,
            playCount = model.profile.playCount
        )

        TitleText(
            title = Res.String.friends,
            modifier = Modifier.padding(start = 16.dp)
        )

        Friends(
            friends = model.friends,
            isMoreLoading = model.isMoreFriendsLoading
        )

        TitleText(
            title = Res.String.loved_tracks,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 4.dp)
        )
    }
}

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
            title = Res.String.scrobbes,
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