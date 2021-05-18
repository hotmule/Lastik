package ru.hotmule.lastik

import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import ru.hotmule.lastik.feature.app.ScrobblerComponent
import ru.hotmule.lastik.utils.AppCoroutineDispatcher

class PlayerCatcherService : NotificationListenerService(), DIAware {

    override val di: DI by closestDI()

    private val scrobblerComponent by instance<ScrobblerComponent>()
    private val serviceScope = CoroutineScope(AppCoroutineDispatcher.Main)

    companion object {
        private const val SCROBBLE_NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()

        val name = ComponentName(this, this::class.java)
        val listener = MediaSessionManager.OnActiveSessionsChangedListener { controllers ->

            controllers?.forEach {

                onPlayStateChanged(it.playbackState)
                onTrackDetected(it.metadata)

                it.registerCallback(object : MediaController.Callback() {

                    override fun onPlaybackStateChanged(state: PlaybackState?) {
                        onPlayStateChanged(state)
                    }

                    override fun onMetadataChanged(metadata: MediaMetadata?) {
                        onTrackDetected(metadata)
                    }
                })
            }
        }

        getSystemService<MediaSessionManager>()?.apply {
            addOnActiveSessionsChangedListener(listener, name)
            listener.onActiveSessionsChanged(getActiveSessions(name))
        }

        serviceScope.launch {
            scrobblerComponent.model.collect {
                if (it.isPlaying) {
                    startForeground(
                        SCROBBLE_NOTIFICATION_ID,
                        NotificationCompat.Builder(
                            this@PlayerCatcherService,
                            getString(R.string.scrobbler_notification_channel_id)
                        )
                            .setContentTitle(it.track)
                            .setContentText(it.artist)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setLargeIcon(it.art)
                            .setOngoing(true)
                            .build()
                    )
                } else {
                    stopForeground(true)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun onPlayStateChanged(state: PlaybackState?) {
        scrobblerComponent.onPlayStateChanged(
            isPlaying = state?.state == PlaybackState.STATE_PLAYING
        )
    }

    private fun onTrackDetected(metadata: MediaMetadata?) {
        scrobblerComponent.onTrackDetected(
            artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST),
            album = metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM),
            track = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE),
            art = metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART),
            duration = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION),
            albumArtist = metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
        )
    }
}