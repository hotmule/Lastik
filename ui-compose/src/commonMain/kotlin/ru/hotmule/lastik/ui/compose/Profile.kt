package ru.hotmule.lastik.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    //interactor: ProfileInteractor
) {
    //val info by interactor.observeInfo().collectAsState(initial = null)
    //val friends by interactor.observeFriends().collectAsState(initial = null)

    Column {

        /*
        ConstraintLayout(modifier = modifier.fillMaxWidth(),
            content = {

                val (image, regDate, playCount) = createRefs()

                ProfileImage(
                    url = info?.highResImage,
                    modifier = Modifier
                        .height(96.dp)
                        .width(96.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top, 24.dp)
                            start.linkTo(parent.start, 24.dp)
                        }
                )

                info?.registerDate?.let {

                    ProfileStat(
                        titleId = R.string.scrobbling_since,
                        subtitle = "", //it.toDateString("d MMMM yyyy"),
                        modifier = Modifier.constrainAs(regDate) {
                            start.linkTo(image.end)
                            top.linkTo(image.top)
                            end.linkTo(playCount.start)
                            bottom.linkTo(image.bottom)
                        }
                    )
                }

                info?.playCount?.let {

                    ProfileStat(
                        titleId = R.string.scrobbles_upper,
                        subtitle = "", //it.toCommasString(),
                        modifier = Modifier.constrainAs(playCount) {
                            start.linkTo(regDate.end)
                            top.linkTo(image.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(image.bottom)
                        }
                    )
                }
            })

        friends?.let {

            TitleText(
                titleId = R.string.friends,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 24.dp
                )
            )

            LazyRow(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                itemsIndexed(it) { index, friend ->
                    Friend(
                        friend = friend,
                        modifier = Modifier.padding(
                            top = 8.dp,
                            start = if (index == 0) 16.dp else 8.dp,
                            end = if (index == it.lastIndex) 16.dp else 8.dp
                        )
                    )
                }
            }
        }

        TitleText(
            titleId = R.string.loved_tracks,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 24.dp
            )
        )
         */
    }
}

@Composable
fun ProfileStat(
    modifier: Modifier = Modifier,
    titleId: Int,
    subtitle: String
) {
    Column(modifier) {

        TitleText(
            titleId = titleId,
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
    titleId: Int
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            modifier = modifier,
            text = "", //stringResource(id = titleId),
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
fun Friend(
    modifier: Modifier = Modifier,
    //friend: Profile
) {
    Column(
        modifier = modifier.width(72.dp)
    ) {

        ProfileImage(
            url = "", //friend.highResImage,
            modifier = Modifier
                .width(72.dp)
                .height(72.dp)
                .fillMaxWidth()
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "", //friend.userName,
                maxLines = 1,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    url: String?
) {
    /*
    CoilImage(
        data = url ?: "",
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.LightGray)
    )
     */
}