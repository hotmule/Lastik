package ru.hotmule.lastik.feature.now.playing

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.instance
import ru.hotmule.lastik.feature.now.playing.store.NowPlayingStore
import ru.hotmule.lastik.feature.now.playing.store.NowPlayingStoreFactory

internal class NowPlayingComponentImpl(directDi: DirectDI) : NowPlayingComponent {

    private val store = NowPlayingStoreFactory(
        storeFactory = directDi.instance(),
        prefs = directDi.instance(),
        api = directDi.instance(),
        db = directDi.instance()
    ).create()

    override val model: Flow<NowPlayingComponent.Model> = store.states.map {
        NowPlayingComponent.Model(
            isPlaying = it.isPlaying,
            track = it.track?.name ?: "UNKNOWN",
            artist = it.track?.albumArtist ?: it.track?.artist ?: "UNKNOWN",
            art = it.track?.art
        )
    }

    override fun onPlayStateChanged(packageName: String, isPlaying: Boolean) {
        store.accept(NowPlayingStore.Intent.CheckPlayState(packageName, isPlaying))
    }

    override fun onTrackDetected(packageName: String, track: NowPlayingComponent.Track) {
        store.accept(NowPlayingStore.Intent.CheckDetectedTrack(packageName, track))
    }
}
