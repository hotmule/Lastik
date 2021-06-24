package ru.hotmule.lastik.feature.app

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.utils.Bitmap

interface NowPlayingComponent {

    data class Track(
        val artist: String? = null,
        val album: String? = null,
        val name: String? = null,
        val art: Bitmap? = null,
        val duration: Long? = null,
        val albumArtist: String? = null
    )

    data class Model(
        val track: Track = Track(),
        val isPlaying: Boolean = false
    )

    val model: Flow<Model>

    fun onPlayStateChanged(isPlaying: Boolean)

    fun onTrackDetected(
        packageName: String,
        track: Track
    )
}