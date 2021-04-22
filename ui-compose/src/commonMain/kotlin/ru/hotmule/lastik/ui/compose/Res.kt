package ru.hotmule.lastik.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.unit.dp

object Res {

    object Dimen {
        val barHeight = 56.dp
    }

    object String {

        const val auth = "Authorization"
        const val username = "Username"
        const val password = "Password"
        const val sign_in = "Sign in"
        const val sign_in_with_last_fm = "Sign in with Last.fm"

        const val updating = "Updating"
        const val no_information = "No information"
        const val scrobbling_now = "Scrobbling now"

        const val scrobbling_since = "Scrobbling since"
        const val friends = "Friends"
        const val loved_tracks = "Loved tracks"
    }

    object Array {

        val shelves = arrayOf(
            "Scrobbles",
            "Artists",
            "Albums",
            "Tracks",
            "Profile"
        )

        val shelfIcons = arrayOf(
            Icons.Rounded.History,
            Icons.Rounded.Face,
            Icons.Rounded.Album,
            Icons.Rounded.Audiotrack,
            Icons.Rounded.AccountCircle
        )

        val periods = arrayOf(
            "All time",
            "Last 7 days",
            "Last 30 days",
            "Last 90 days",
            "Last 180 days",
            "Last 365 days",
        )
    }
}