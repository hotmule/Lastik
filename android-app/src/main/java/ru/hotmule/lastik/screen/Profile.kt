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
import ru.hotmule.lastik.domain.ProfileInteractor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import ru.hotmule.lastik.components.ProfileImage
import ru.hotmule.lastik.utlis.toDateString
import ru.hotmule.lastik.utlis.toCommasString

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

        val (image, regDate, playCount) = createRefs()

        ProfileImage(
            url = info?.highResImage,
            modifier = Modifier
                .preferredHeight(90.dp)
                .preferredWidth(90.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 20.dp)
                    bottom.linkTo(parent.bottom, 20.dp)
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
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun ProfileStatPreview() = ProfileStat(
    titleId = R.string.scrobbles_upper,
    subtitle = "111111"
)