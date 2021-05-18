package ru.hotmule.lastik.ui.compose.res

import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Res {

    object Dimen {
        val barHeight = 56.dp
        val shelfItemHeight = 82.dp
    }

    object Color {

        //red
        private val sangria = Color(0xff9d0000)
        private val crimson = Color(0xffd50000)

        //yellow
        val sunflower = Color(0xffffc900)

        val lights = lightColors(primary = sangria, secondary = crimson)
        val darks = darkColors(primary = crimson)
    }

    object String {

        const val auth = "Authorization"
        const val username = "Username"
        const val password = "Password"
        const val sign_in = "Sign in"
        const val sign_in_with_last_fm = "Sign in with Last.fm"

        const val scrobbling_since = "Scrobbling since"
        const val scrobbes = "Scrobbles"
        const val friends = "Friends"
        const val loved_tracks = "Loved tracks"

        const val logging_out = "Logging Out"
        const val logging_out_confirmation = "Are you sure you want to log out?"
        const val confirm = "Confirm"
        const val cancel = "Cancel"
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
            "Last 365 days",
            "Last 180 days",
            "Last 90 days",
            "Last 30 days",
            "Last 7 days"
        )

        val profileMenu = arrayOf(
            "Settings",
            "Log Out"
        )
    }
}