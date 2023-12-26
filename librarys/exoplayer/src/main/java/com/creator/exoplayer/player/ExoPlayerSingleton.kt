package com.creator.exoplayer.player

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


object ExoPlayerSingleton {
    private const val TAG = "ExoPlayerSingleton"
    private lateinit var exoPlayer: ExoPlayer

    fun getExoPlayer(context: Context): ExoPlayer {
        exoPlayer = ExoPlayer.Builder(context).build();
        exoPlayer.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                val currentPosition = exoPlayer.currentPosition
                Log.d(TAG, "当前时间:::$currentPosition")
            }
        })
        return exoPlayer
    }

    fun setSource(uri: String, context: Context) {
        val mediaItem = MediaItem.fromUri(uri)
        val mediaSource: MediaSource =
            ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(
                    context,
                    "com.creator.artplayer"
                )
            ).createMediaSource(mediaItem)
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
//        exoPlayer.playWhenReady = true;
    }

    fun play() {
        exoPlayer.play()
    }


}