package ru.hotmule.lastik.feature.app

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.utils.Bitmap

interface NowPlayingComponent {

    data class Track(
        var artist: String? = null,
        var album: String? = null,
        var name: String? = null,
        val art: Bitmap? = null,
        val duration: Long? = null,
        var albumArtist: String? = null
    )

    data class Model(
        val isPlaying: Boolean = false,
        val track: String = "",
        val artist: String = "",
        val art: Bitmap? = null
    )

    val model: Flow<Model>

    fun onPlayStateChanged(
        packageName: String,
        isPlaying: Boolean
    )

    fun onTrackDetected(
        packageName: String,
        track: Track
    )
}