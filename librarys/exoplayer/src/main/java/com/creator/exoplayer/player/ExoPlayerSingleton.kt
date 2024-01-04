package com.creator.exoplayer.player

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.creator.common.Constants
import com.creator.common.MainThreadExecutor
import com.creator.common.bean.VideoTransmitBean
import com.creator.common.enums.Enums
import com.creator.common.utils.IPUtil
import com.creator.common.utils.LogUtil
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.Listener
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.net.URI


@SuppressLint("StaticFieldLeak")
object ExoPlayerSingleton {
    private const val TAG = "ExoPlayerSingleton"
    private lateinit var exoPlayer: ExoPlayer
    fun getExoPlayer(context: Context, ): ExoPlayer {
        Log.setLogLevel(Log.LOG_LEVEL_ALL)
        exoPlayer = ExoPlayer.Builder(context).build();
        return exoPlayer
    }

    fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }
    fun addListener(listener: Listener){
        exoPlayer.addListener(listener);
    }

    /*  fun setSource(uri: String, context: Context, isPlayWhenReady: Boolean = false) {
          val mediaItem = MediaItem.fromUri(uri)
          val mediaSource: MediaSource =
              ProgressiveMediaSource.Factory(
                  DefaultDataSourceFactory(
                      context,
                      "com.creator.eternalbonds"
                  )
              ).createMediaSource(mediaItem)


          exoPlayer.setMediaSource(mediaSource)
          exoPlayer.prepare()
          if (isPlayWhenReady) exoPlayer.playWhenReady = true;
      }*/
    fun setSource(uri: String, context: Context, isPlayWhenReady: Boolean = false) {
        // 创建一个MediaItem对象，表示要播放的媒体文件
        val mediaItem = MediaItem.fromUri(uri)
        // 设置数据源工厂
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "com.creator.eternalbonds")
        )
        // 创建一个ProgressiveMediaSource，用于播放常规的媒体文件（例如MP4）
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        // 设置要播放的媒体项到ExoPlayer
        exoPlayer.setMediaItem(mediaItem)
        // 设置媒体源工厂到ExoPlayer
        exoPlayer.setMediaSource(mediaSource)
        // 准备播放
        exoPlayer.prepare()
        if (isPlayWhenReady) exoPlayer.playWhenReady = true;
    }

    fun play() {
        exoPlayer.play()
    }

    fun seekTo(l: Long) {
        LogUtil.d(TAG, "播放" + l.toString())
        MainThreadExecutor.runOnUiThread {
            exoPlayer.seekTo(l)
        }
    }


    /*object MyWebSocket {
        private lateinit var websocket: WebSocket

        init {
            //根据播放器角色创建对应的websocket类
            when (playerRole) {
                Enums.PlayerRole.Client -> {
                    ExoPlayerWebSocketClient().connect()
                }

                Enums.PlayerRole.Server -> {
                    ExoPlayerWebSocketServer().start()
                }
            }
        }

        *//**
         * 播放器客户端
         *//*
        class ExoPlayerWebSocketClient constructor(uri: String = websocketUri) :
            org.java_websocket.client.WebSocketClient(URI(uri)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                websocket = connection
                LogUtil.d(TAG, "onOpen")
            }

            override fun onMessage(message: String?) {
                val videoTransmitBean = VideoTransmitBean.toClass(message)
                MainThreadExecutor.runOnUiThread {
                    if (videoUri == null && videoUri != videoTransmitBean.uri) {
                        setSource(videoTransmitBean.uri, context, true)
                        videoUri = videoTransmitBean.uri
                    }
                    LogUtil.d(TAG, videoUri.toString())
                    seekTo(videoTransmitBean.currentPosition)
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                LogUtil.e(TAG, "onError$reason")
            }

            override fun onError(ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }
        }


        *//**
         * 播放器服务端
         *//*
        class ExoPlayerWebSocketServer constructor(port: Int = Constants.WebSocket.PORT) :
            WebSocketServer(InetSocketAddress(port)) {
            override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
                websocket = conn

                //websocket客户端连接后，向客户端发送视频信息
                MainThreadExecutor.runOnUiThread {
                    val videoTransmitBean = VideoTransmitBean()
                    videoTransmitBean.currentPosition = exoPlayer.currentPosition
                    LogUtil.d(
                        TAG,
                        " onOpen:::" + exoPlayer.currentMediaItem?.playbackProperties?.uri.toString()
                    )
                    videoTransmitBean.uri = videoUri
                    send(videoTransmitBean)
                }
            }

            override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            }

            override fun onMessage(conn: WebSocket?, message: String?) {
                LogUtil.d(TAG, "onMessage:::$message")
                val toClass = VideoTransmitBean.toClass(message)
                seekTo(toClass.currentPosition)
            }

            override fun onError(conn: WebSocket?, ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }

            override fun onStart() {
                LogUtil.d(TAG, "onStart")
                MainThreadExecutor.runOnUiThread {
                    Toast.makeText(context, "WebSocket服务启动成功", Toast.LENGTH_SHORT).show()
                }

            }
        }


        fun send(videoTransmitBean: VideoTransmitBean) {
            try {
                websocket.send(videoTransmitBean.toString())
            } catch (e: Exception) {
                LogUtil.e(TAG, "send失败：${videoTransmitBean.toString()}\n" + e.message, e)
            }
        }


    }*/

}
