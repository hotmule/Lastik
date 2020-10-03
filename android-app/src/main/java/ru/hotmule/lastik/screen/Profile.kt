package ru.hotmule.lastik.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.launchInComposition
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.hotmule.lastik.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import ru.hotmule.lastik.Sdk
import ru.hotmule.lastik.components.LibraryList
import ru.hotmule.lastik.components.ProfileImage
import ru.hotmule.lastik.data.local.Profile
import ru.hotmule.lastik.utlis.toDateString
import ru.hotmule.lastik.utlis.toCommasString

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    isUpdating: (Boolean) -> Unit,
    sdk: Sdk
) {

    launchInComposition {
        isUpdating.invoke(true)
        try {
            sdk.profileInteractor.refreshProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isUpdating.invoke(false)
    }

    val info by sdk.profileInteractor.observeInfo().collectAsState(initial = null)
    val friends by sdk.profileInteractor.observeFriends().collectAsState(initial = null)

    ScrollableColumn {

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {

            val (image, regDate, playCount) = createRefs()

            ProfileImage(
                url = info?.highResImage,
                modifier = Modifier
                    .preferredHeight(90.dp)
                    .preferredWidth(90.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top, 24.dp)
                        start.linkTo(parent.start, 24.dp)
                    }
            )

            info?.registerDate?.let {

                ProfileStat(
                    titleId = R.string.scrobbling_since,
                    subtitle = it.toDateString("d MMMM yyyy"),
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
                    subtitle = it.toCommasString(),
                    modifier = Modifier.constrainAs(playCount) {
                        start.linkTo(regDate.end)
                        top.linkTo(image.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(image.bottom)
                    }
                )
            }
        }

        friends?.let {
            ScrollableRow(
                modifier = Modifier.padding(8.dp)
            ) {
                it.forEach {
                    Friend(friend = it)
                }
            }
        }

        LibraryList(
            isUpdating = isUpdating,
            scrollingEnabled = false,
            refresh = sdk.tracksInteractor::refreshLovedTracks,
            itemsFlow = sdk.tracksInteractor::observeLovedTracks,
            modifier = modifier
        )
    }
}

@Composable
fun ProfileStat(
    modifier: Modifier = Modifier,
    @StringRes titleId: Int,
    subtitle: String
) {
    Column(modifier) {

        ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Text(
            text = subtitle,
            fontSize = TextUnit(6),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun Friend(
    modifier: Modifier = Modifier,
    friend: Profile
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .preferredWidth(72.dp)
    ) {

        ProfileImage(
            url = friend.highResImage,
            modifier = Modifier
                .preferredHeight(72.dp)
                .fillMaxWidth()
        )

        ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
            Text(
                text = friend.userName,
                maxLines = 1,
                fontSize = 12.sp,
                modifier = modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun ProfileStatPreview() = ProfileStat(
    titleId = R.string.scrobbles_upper,
    subtitle = "111111"
)