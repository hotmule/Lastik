package ru.hotmule.lastik.feature.app

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.instance
import ru.hotmule.lastik.feature.app.NowPlayingComponent.*
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.feature.app.store.NowPlayingStoreFactory

internal class NowPlayingComponentImpl(directDi: DirectDI) : NowPlayingComponent {

    private val store = NowPlayingStoreFactory(
        storeFactory = directDi.instance(),
        prefs = directDi.instance(),
        api = directDi.instance(),
        db = directDi.instance()
    ).create()

    override val model: Flow<Model> = store.states.map {
        Model(
            isPlaying = it.isPlaying,
            track = it.track?.name ?: "UNKNOWN",
            artist = it.track?.albumArtist ?: it.track?.artist ?: "UNKNOWN",
            art = it.track?.art
        )
    }

    override fun onPlayStateChanged(packageName: String, isPlaying: Boolean) {
        store.accept(Intent.CheckPlayState(packageName, isPlaying))
    }

    override fun onTrackDetected(packageName: String, track: Track) {
        store.accept(Intent.CheckDetectedTrack(packageName, track))
    }
}