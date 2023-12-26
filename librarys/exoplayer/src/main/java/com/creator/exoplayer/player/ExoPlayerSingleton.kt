package com.creator.exoplayer.player

import android.content.Context
import android.util.Log
import com.creator.common.bean.VideoTransmitBean
import com.creator.websocket.client.WebSocketClient
import com.creator.websocket.server.WebSocketServer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import org.java_websocket.AbstractWebSocket
import org.java_websocket.WebSocket
import java.nio.ByteBuffer


object ExoPlayerSingleton {
    private const val TAG = "ExoPlayerSingleton"
    private lateinit var _exoPlayer: ExoPlayer
    private lateinit var exoPlayer: ExoPlayer
//        get() = _exoPlayer
    private lateinit var websocket: AbstractWebSocket
    fun getExoPlayer(context: Context): ExoPlayer {
        exoPlayer = ExoPlayer.Builder(context).build();
        exoPlayer.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                val currentPosition = exoPlayer.currentPosition
                val videoTransmitBean = VideoTransmitBean()
                videoTransmitBean.currentPosition = currentPosition
                Log.d(TAG, "当前时间:::$currentPosition")
                //发送当前变更位置

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

    object ExoPlayerWebSocketServer : WebSocketServer() {
        override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
            super.onMessage(conn, message)
        }
    }

    class ExoPlayerWebSocketClient private constructor(uri: String, tag: String) :
        WebSocketClient(uri, tag) {
        companion object {
            private var instance: ExoPlayerWebSocketClient? = null
            fun getInstance(uri: String, tag: String): ExoPlayerWebSocketClient {
                return instance ?: synchronized(this) {
                    instance ?: ExoPlayerWebSocketClient(uri, tag).also { instance = it }
                }
            }
        }
    }


}
