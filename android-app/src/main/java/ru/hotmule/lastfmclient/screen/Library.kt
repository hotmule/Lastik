package ru.hotmule.lastfmclient.screen

import android.compose.utils.navigationBarsPadding
import android.compose.utils.statusBarsPadding
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.hotmule.lastfmclient.R
import ru.hotmule.lastfmclient.Sdk
import ru.hotmule.lastfmclient.domain.AuthInteractor

sealed class Library(val titleStringId: Int) {
    class Scrobbles : Library(R.string.scrobbles)
    class Artists : Library(R.string.artists)
    class Albums : Library(R.string.albums)
    class Tracks : Library(R.string.tracks)
    class Profile : Library(R.string.profile)
}

@Composable
fun LibraryScreen(sdk: Sdk) {

    var currentItem by remember { mutableStateOf<Library>(Library.Scrobbles()) }

    val title: String
    val body: @Composable (InnerPadding) -> Unit

    when (currentItem) {
        is Library.Profile -> {
            title = sdk.userInteractor.getName()
            body = { Profile(sdk.authInteractor) }
        }
        else -> {
            title = stringResource(id = currentItem.titleStringId)
            body = { LibraryListItem(title) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.preferredHeight(79.dp),
                title = {
                    Text(
                        modifier = Modifier.statusBarsPadding(),
                        text = title
                    )
                }
            )
        },
        bodyContent = body,
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.preferredHeight(75.dp),
            ) {
                listOf(
                    Library.Scrobbles() to Icons.Rounded.History,
                    Library.Artists() to Icons.Rounded.Face,
                    Library.Albums() to Icons.Rounded.Album,
                    Library.Tracks() to Icons.Rounded.Audiotrack,
                    Library.Profile() to Icons.Rounded.Person
                ).forEach {
                    BottomNavigationItem(
                        modifier = Modifier.navigationBarsPadding(bottom = true),
                        icon = { Icon(it.second) },
                        label = { Text(stringResource(id = it.first.titleStringId)) },
                        onClick = { currentItem = it.first },
                        selected = currentItem == it.first
                    )
                }
            }
        }
    )
}

@Composable
private fun Profile(interactor: AuthInteractor) {
    Button(
        onClick = { interactor.signOut() }
    ) {
        Text(stringResource(id = R.string.sign_out))
    }
}

@Composable
private fun LibraryListItem(title: String) {
    LazyColumnFor(items = mutableListOf<String>().apply {
        for (i in 1..100) add("$title $i")
    }) {
        listItem(text = it)
    }
}

@Composable
private fun listItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .clickable(onClick = { })
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                asset = Icons.Rounded.Album,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier.gravity(align = Alignment.CenterVertically)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
                ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        Divider()
    }
}