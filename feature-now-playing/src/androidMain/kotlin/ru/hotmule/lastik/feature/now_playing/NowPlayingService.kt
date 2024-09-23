package ru.hotmule.lastik.feature.now_playing

import Lastik.R
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import coil3.asImage
import coil3.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import ru.hotmule.lastik.feature.app.NowPlayingComponent
import ru.hotmule.lastik.feature.app.NowPlayingComponent.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

class NowPlayingService : NotificationListenerService(), DIAware {

    companion object {
        private const val SCROBBLE_NOTIFICATION_ID = 1
    }

    override val di: DI by closestDI()

    private val nowPlayingComponent by instance<NowPlayingComponent>()
    private val serviceScope = CoroutineScope(AppCoroutineDispatcher.Main)

    private var mediaControllers: List<MediaController>? = null

    override fun onCreate() {
        super.onCreate()

        catchPlayers()
        provideNowPlaying()
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
            track = Track(
                metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST),
                metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM),
                metadata?.getString(MediaMetadata.METADATA_KEY_TITLE),
                metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)?.asImage(),
                metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION),
                metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
            )
        )
    }

    private fun provideNowPlaying() {

        serviceScope.launch {
            nowPlayingComponent.model.collect {

                if (it.isPlaying) {
                    startForeground(
                        SCROBBLE_NOTIFICATION_ID,
                        nowPlayingNotification(it.track, it.artist, it.art?.toBitmap())
                    )
                } else {
                    stopForeground(true)
                }
            }
        }
    }

    private fun nowPlayingNotification(
        track: String,
        artist: String,
        art: Bitmap?,
    ) = NotificationCompat.Builder(this, getString(R.string.scrobbler_notification_channel_id))
            .setContentTitle(track)
            .setContentText(artist)
            .setSmallIcon(R.drawable.ic_now_playing)
            .setLargeIcon(art)
            .setOngoing(true)
            .build()
}