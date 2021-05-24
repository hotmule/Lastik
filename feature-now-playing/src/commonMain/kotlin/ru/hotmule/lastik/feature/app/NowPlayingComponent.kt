package ru.hotmule.lastik.feature.app

import kotlinx.coroutines.flow.Flow
import ru.hotmule.lastik.utils.Bitmap

interface NowPlayingComponent {

    data class Model(
        val isPlaying: Boolean = false,
        val artist: String = "",
        val album: String? = null,
        val track: String = "",
        val art: Bitmap? = null
    )

    val model: Flow<Model>

    fun onPlayStateChanged(isPlaying: Boolean?)

    fun onTrackDetected(
        artist: String?,
        album: String?,
        track: String?,
        art: Bitmap?,
        duration: Long?,
        albumArtist: String?
    )
}