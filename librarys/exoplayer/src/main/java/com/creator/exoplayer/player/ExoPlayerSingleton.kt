package com.creator.exoplayer.player

import android.content.Context
import android.util.Log
import com.creator.common.Constants
import com.creator.common.bean.VideoTransmitBean
import com.creator.websocket.client.WebSocketClient
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer


object ExoPlayerSingleton {
    private const val TAG = "ExoPlayerSingleton"
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var websocket: WebSocket
    fun getExoPlayer(context: Context, isServer: Boolean): ExoPlayer {
        exoPlayer = ExoPlayer.Builder(context).build();
        if (isServer) {
            ExoPlayerWebSocketServer.start()
        } else {
            websocket = ExoPlayerWebSocketClient.getInstance(
                "ws://192.168.3.3:${Constants.WebSocket.PORT}",
                TAG
            )
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                val currentPosition = exoPlayer.currentPosition
                val videoTransmitBean = VideoTransmitBean()
                videoTransmitBean.currentPosition = currentPosition
                Log.d(TAG, "当前时间:::$currentPosition")
                //发送当前变更位置
                websocket.send(videoTransmitBean.toString())
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

    fun seekTo(l: Long) {
        Log.d("WebSocket","播放"+l.toString())
        exoPlayer.seekTo(l)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    object ExoPlayerWebSocketServer : WebSocketServer(InetSocketAddress(Constants.WebSocket.PORT)) {
        //         lateinit var websocket:WebSocket
        override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
            super.onMessage(conn, message)
            val toString = message.toString()
            val toClass = VideoTransmitBean.toClass(toString)
            seekTo(toClass.currentPosition)
        }

        override fun onError(conn: WebSocket?, ex: Exception?) {
            Log.d("WebSocket","onError")
        }

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
            websocket = conn
        }

        override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            Log.d("WebSocket","onClose")
        }

        override fun onMessage(conn: WebSocket?, message: String?) {
        }

        override fun onStart() {
            Log.d("WebSocket","onStart")
        }
    }

    class ExoPlayerWebSocketClient private constructor(uri: String, tag: String) :
        WebSocketClient(uri, tag) {

        override fun onMessage(bytes: ByteBuffer?) {
            super.onMessage(bytes)
            val toString = bytes.toString()
            VideoTransmitBean.toClass(toString)
            Log.d("WebSocket","SSSSSSS")
        }

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
