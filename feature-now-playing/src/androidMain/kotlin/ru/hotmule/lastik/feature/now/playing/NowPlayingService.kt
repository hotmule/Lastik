package ru.hotmule.lastik.feature.now.playing

import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import androidx.core.content.getSystemService
import coil3.asImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

class NowPlayingService : NotificationListenerService(), DIAware {

    override val di: DI by closestDI()

    private val nowPlayingComponent by instance<NowPlayingComponent>()
    private val serviceScope = CoroutineScope(AppCoroutineDispatcher.Main)

    private var mediaControllers: List<MediaController>? = null

    override fun onCreate() {
        super.onCreate()
        catchPlayers()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun catchPlayers() {

        val listener = MediaSessionManager.OnActiveSessionsChangedListener { controllers ->

            mediaControllers = controllers
            mediaControllers?.forEach {

                onTrackDetected(it.packageName, it.metadata)
                onPlayStateChanged(it.packageName, it.playbackState)

                it.registerCallback(
                    object : MediaController.Callback() {

                        override fun onPlaybackStateChanged(state: PlaybackState?) {
                            onPlayStateChanged(it.packageName, state)
                        }

                        override fun onMetadataChanged(metadata: MediaMetadata?) {
                            onTrackDetected(it.packageName, metadata)
                        }
                    }
                )
            }
        }

        val name = ComponentName(this, this::class.java)

        getSystemService<MediaSessionManager>()?.apply {
            addOnActiveSessionsChangedListener(listener, name)
            listener.onActiveSessionsChanged(getActiveSessions(name))
        }
    }

    private fun onPlayStateChanged(
        packageName: String,
        state: PlaybackState?
    ) {
        nowPlayingComponent.onPlayStateChanged(
            packageName = packageName,
            isPlaying = state?.state == PlaybackState.STATE_PLAYING
        )
    }

    private fun onTrackDetected(
        packageName: String,
        metadata: MediaMetadata?
    ) {
        nowPlayingComponent.onTrackDetected(
            packageName = packageName,
            track = NowPlayingComponent.Track(
                metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST),
                metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM),
                metadata?.getString(MediaMetadata.METADATA_KEY_TITLE),
                metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)?.asImage(),
                metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION),
                metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
            )
        )
    }
}
