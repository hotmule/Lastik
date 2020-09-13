package ru.hotmule.lastfmclient

import android.compose.utils.Navigator
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Destination : Parcelable {
    @Parcelize object Library : Destination()
    @Parcelize object Auth : Destination()
}

class Actions(navigator: Navigator<Destination>) {
    val toBack: () -> Unit = { navigator.back() }
    val toAuth: () -> Unit = { navigator.navigate(Destination.Auth) }
}