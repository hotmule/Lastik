package ru.hotmule.lastik.feature.app

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.instance
import ru.hotmule.lastik.feature.app.ScrobblerComponent.*
import ru.hotmule.lastik.feature.app.store.ScrobblerStore.*
import ru.hotmule.lastik.feature.app.store.ScrobblerStoreFactory
import ru.hotmule.lastik.utils.Bitmap

internal class ScrobblerComponentImpl(directDi: DirectDI) : ScrobblerComponent {

    private val store = ScrobblerStoreFactory(directDi.instance()).create()

    override val model: Flow<Model> = store.states.map {
        Model(
            isPlaying = it.isPlaying,
            artist = it.artist,
            album = it.album,
            track = it.track,
            art = it.art
        )
    }

    override fun onPlayStateChanged(isPlaying: Boolean?) {
        store.accept(Intent.CheckPlayState(isPlaying))
    }

    override fun onTrackDetected(
        artist: String?,
        album: String?,
        track: String?,
        art: Bitmap?,
        duration: Long?,
        albumArtist: String?
    ) {
        store.accept(Intent.CheckDetectedTrack(artist, album, track, art, duration, albumArtist))
    }
}