package ru.hotmule.lastik

import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import androidx.core.content.getSystemService

class PlayerCatcherService : NotificationListenerService() {

    companion object {

        const val TRACK_DETECTED_ACTION = "trackDetected"

        const val IS_PLAYING = "isPlaying"
        const val ARTIST_ARG = "artist"
        const val TRACK_ARG = "track"
        const val TIME_ARG = "time"
    }

    override fun onBind(intent: Intent?) = super.onBind(intent)

    override fun onCreate() {
        super.onCreate()

        val name = ComponentName(this, this::class.java)
        val listener = MediaSessionManager.OnActiveSessionsChangedListener { controllers ->

            controllers
                ?.find { it.packageName == "com.google.android.apps.youtube.music" }
                ?.let {
                    onControllerChanged(it)
                    it.registerCallback(object : MediaController.Callback() {

                        override fun onPlaybackStateChanged(state: PlaybackState?) {
                            onControllerChanged(it)
                        }
                        override fun onMetadataChanged(metadata: MediaMetadata?) {
                            onControllerChanged(it)
                        }
                    })
                }
        }

        getSystemService<MediaSessionManager>()?.apply {
            addOnActiveSessionsChangedListener(listener, name)
            listener.onActiveSessionsChanged(getActiveSessions(name))
        }
    }

    private fun onControllerChanged(controller: MediaController) {

        val metadata = controller.metadata

        val artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST)
        val album = metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM)
        val artwork = metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
        val artworkUri = metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)
        val track = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)
        val duration = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)

        sendBroadcast(
            Intent().apply {
                action = TRACK_DETECTED_ACTION
                putExtra(IS_PLAYING, controller.playbackState?.state == PlaybackState.STATE_PLAYING)
                putExtra(ARTIST_ARG, metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST))
                putExtra(TRACK_ARG, metadata?.getString(MediaMetadata.METADATA_KEY_TITLE))
            }
        )
    }
}