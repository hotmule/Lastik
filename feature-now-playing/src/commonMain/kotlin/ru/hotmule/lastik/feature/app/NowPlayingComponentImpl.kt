package ru.hotmule.lastik.feature.app

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kodein.di.DirectDI
import org.kodein.di.instance
import ru.hotmule.lastik.feature.app.NowPlayingComponent.*
import ru.hotmule.lastik.feature.app.store.NowPlayingStore.*
import ru.hotmule.lastik.feature.app.store.NowPlayingStoreFactory
import ru.hotmule.lastik.utils.Bitmap

internal class NowPlayingComponentImpl(directDi: DirectDI) : NowPlayingComponent {

    private val store = NowPlayingStoreFactory(
        storeFactory = directDi.instance(),
        prefsStore = directDi.instance()
    ).create()

    override val model: Flow<Model> = store.states.map {
        Model(
            track = it.track,
            isPlaying = it.isPlaying
        )
    }

    override fun onPlayStateChanged(isPlaying: Boolean) {
        store.accept(Intent.CheckPlayState(isPlaying))
    }

    override fun onTrackDetected(packageName: String, track: Track) {
        store.accept(Intent.CheckDetectedTrack(packageName, track))
    }
}