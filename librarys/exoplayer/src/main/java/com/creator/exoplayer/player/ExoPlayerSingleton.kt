package com.creator.exoplayer.player

import MainThreadExecutor
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.creator.common.Constants
import com.creator.common.bean.VideoTransmitBean
import com.creator.common.enums.Enums
import com.creator.common.utils.LogUtil
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
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
    private lateinit var videoRole: Enums.VideoRole
    private var videoUri: String? = null

    //    private var videoUri = MutableLiveData<String>()
    private lateinit var websocketUri: String
    private lateinit var context: Context
    private var isSeekTo: Boolean = false
    fun getExoPlayer(
        context: Context,
        videoRole: Enums.VideoRole,
        websocketUri: String = ""
    ): ExoPlayer {
        exoPlayer = ExoPlayer.Builder(context).build();
        this.websocketUri = websocketUri
        this.context = context
        this.videoRole = videoRole
        exoPlayer.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                if (!isSeekTo) {
                    val currentPosition = exoPlayer.currentPosition
                    val videoTransmitBean = VideoTransmitBean()
                    videoTransmitBean.currentPosition = currentPosition
                    videoTransmitBean.uri =
                        exoPlayer.currentMediaItem?.playbackProperties?.uri.toString()
                    videoTransmitBean.uri =
                        "http://" +   "[2409:8a34:2072:3a00:cbcd:8d5c:429a:df9b]:8088/video"
                    LogUtil.d(TAG, "当前时间:::$currentPosition")
                    //发送当前变更位置
                    MyWebSocket.send(videoTransmitBean)
                }
                isSeekTo = false
            }
        })

        MyWebSocket
        return exoPlayer
    }

    fun setSource(uri: String, context: Context, isPlayWhenReady: Boolean = false) {
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
    }

    fun play() {
        exoPlayer.play()
    }

    fun seekTo(l: Long) {
        LogUtil.d(TAG, "播放" + l.toString())
        isSeekTo = true
        MainThreadExecutor.runOnUiThread {
            exoPlayer.seekTo(l)
        }
    }


    object MyWebSocket {
        private lateinit var websocket: WebSocket

        init {
            when (videoRole) {
                Enums.VideoRole.Client -> {
                    ExoPlayerWebSocketClient().connect()
                }

                Enums.VideoRole.Server -> {
                    ExoPlayerWebSocketServer().start()
                }
            }
        }

        class ExoPlayerWebSocketClient constructor(uri: String = websocketUri) :
            org.java_websocket.client.WebSocketClient(URI(uri)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                websocket = connection
                LogUtil.d(TAG, "onOpen")
            }

            override fun onMessage(message: String?) {
                val videoTransmitBean = VideoTransmitBean.toClass(message)
                MainThreadExecutor.runOnUiThread {
                    if (videoUri == null) {
                        setSource(videoTransmitBean.uri, context, true)
                        videoUri = videoTransmitBean.uri
                        play()
                    }

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

        class ExoPlayerWebSocketServer constructor(port: Int = Constants.WebSocket.PORT) :
            WebSocketServer(InetSocketAddress(port)) {
            override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
                websocket = conn

                MainThreadExecutor.runOnUiThread {
                    val videoTransmitBean = VideoTransmitBean()
                    videoTransmitBean.currentPosition = exoPlayer.currentPosition
                    LogUtil.d(
                        TAG,
                        " onOpen:::" + exoPlayer.getCurrentMediaItem()?.playbackProperties?.uri.toString()
                    )
                    /*videoTransmitBean.uri =
                        exoPlayer.getCurrentMediaItem()?.playbackProperties?.uri.toString()*/
                    videoTransmitBean.uri =
                        "[2409:8a34:2072:3a00:cbcd:8d5c:429a:df9b]:8088/video"
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


    }

}
