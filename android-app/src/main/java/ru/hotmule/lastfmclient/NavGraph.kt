package ru.hotmule.lastfmclient

import android.compose.utils.Navigator
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Destination : Parcelable {
    @Parcelize object Library : Destination()
    @Parcelize object Auth : Destination()
    @Parcelize object AuthDialog: Destination()
}

class Actions(navigator: Navigator<Destination>) {
    val logIn: () -> Unit = { navigator.navigate(Destination.AuthDialog) }
    val logOut: () -> Unit = { navigator.navigate(Destination.Auth) }
    val upPress: () -> Unit = { navigator.back() }
}