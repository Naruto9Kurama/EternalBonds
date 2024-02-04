package com.creator.websocket

import android.content.Context
import com.creator.common.Constants
import com.creator.common.bean.WebSocketMessageBean
import com.creator.common.bean.player.VideoPlayerDataBean
import com.creator.common.enums.Enums
import com.creator.common.utils.LogUtil
import com.creator.common.utils.ToastUtil
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.net.URI

class VideoWebSocket(val context: Context,val isServer:Boolean,val videoPlayerDataBean: VideoPlayerDataBean) {
    private val TAG="VideoWebSocket"
    private var websockets = HashSet<WebSocket>()
    private var isReadyMap = HashMap<WebSocket, Boolean>()
    private lateinit var exoPlayerWebSocketServer: ExoPlayerWebSocketServer
    private lateinit var exoPlayerWebSocketClient: ExoPlayerWebSocketClient

    fun send(message:String){
        websockets.forEach {
            it.send(message)
        }
    }
    init {
        //根据播放器角色创建对应的websocket类
        if (isServer) {
            exoPlayerWebSocketServer = ExoPlayerWebSocketServer()
            exoPlayerWebSocketServer.start()
        } else {
            exoPlayerWebSocketClient = ExoPlayerWebSocketClient()
            exoPlayerWebSocketClient.connect()
        }
    }

    inner class ExoPlayerWebSocketClient constructor(uri: String = videoPlayerDataBean.serverIp) :
        org.java_websocket.client.WebSocketClient(URI(uri)) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            websockets.add(connection)
            ToastUtil.show(context, "连接成功")
        }

        override fun onMessage(message: String?) {
            LogUtil.d(TAG, message.toString())
            processMessages(message)
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            LogUtil.e(TAG, "onClose$reason")
            websockets.clear()
            if (remote) {
                ToastUtil.show(context, "对方已断开连接")
//                activity?.finish()
            } else {
                ToastUtil.show(context, "重新连接")
                connect()
            }
        }

        override fun onError(ex: java.lang.Exception?) {
            LogUtil.e(TAG, "onError:::" + ex?.message, ex)
        }
    }


    inner class ExoPlayerWebSocketServer constructor(port: Int = Constants.WebSocket.PORT) :
        WebSocketServer(InetSocketAddress("::", port)) {
        override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
            websockets.add(conn)
            isReadyMap[conn] = false;
//                send()
        }

        override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            if (remote) {
                ToastUtil.show(context, "${conn.toString()}已断开连接")
                websockets.remove(conn)
            } else {
                ToastUtil.show(context, "${conn.toString()}已断开连接")

//                activity?.finish()
            }
        }

        override fun onMessage(conn: WebSocket?, message: String?) {
            LogUtil.d(TAG, "onMessage:::$message")
            processMessages(message, conn)
        }

        override fun onError(conn: WebSocket?, ex: java.lang.Exception?) {
            LogUtil.e(TAG, "onError:::" + ex?.message, ex)
        }

        override fun onStart() {
            LogUtil.d(TAG, "onStart")
            ToastUtil.show(context, "WebSocket服务启动成功")
        }

        override fun start() {
            try {
                super.start()
            } catch (e: Exception) {
                ToastUtil.show(context, "websocket服务端口被占用,无法启动成功")
//                activity?.finish()
            }
        }
    }

    fun close() {
        kotlin.runCatching {
            //根据播放器角色创建对应的websocket类
            if (isServer) {
                exoPlayerWebSocketServer.stop()

            } else {
                exoPlayerWebSocketClient.close()

            }
//                myWebSocket = null
            ToastUtil.show(context, "已关闭websocket")
        }
    }

    fun processMessages(message: String?, conn: WebSocket? = null) {
        val webSocketMessageBean = WebSocketMessageBean.toClass(message)
        when (webSocketMessageBean.messageType) {
            Enums.MessageType.IS_READY -> {
                isReadyMap[conn!!] = true
                var isReady = 0
                isReadyMap.forEach {
                    if (it.value) isReady++
                }
                if (isReady == isReadyMap.size) {
//                        send(Enums.MessageType.START_PLAY, "")
//                    playerController.play()
                }
            }

            Enums.MessageType.Video -> {
                updateVideo(webSocketMessageBean.message)
            }

            Enums.MessageType.START_PLAY -> {
//                playerController.play()
            }
        }
    }

    /**
     * 通过websocket接收到的消息更新视频数据
     */
    fun updateVideo(message: String?) {
//            videoPlayerParams = VideoPlayerParams.getInstance().toClass(message)
//            LogUtil.d(TAG, videoPlayerParams.toString())
//            if (videoPlayerParams != null) {
//                startPlay()
//
//            }
    }

    /*fun send(videoPlayerParams: VideoPlayerParams) {
        send(Enums.MessageType.Video, videoPlayerParams.toString())
    }

    fun send(messageType: Enums.MessageType, message: String) {
        send(WebSocketMessageBean(messageType, message))
    }

    fun send(webSocketMessageBean: WebSocketMessageBean) {
        websockets.forEach { websocket ->
            try {
                websocket.send(webSocketMessageBean.toString())
            } catch (e: Exception) {
                LogUtil.e(TAG, "send失败：${webSocketMessageBean.toString()}\n" + e.message, e)
            }
        }
    }

    fun send() {
        send(videoPlayerParams)
    }*/
}