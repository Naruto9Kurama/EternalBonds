package com.creator.websocket

import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.websocket.client.MyWebSocketClient
import com.creator.websocket.server.MyWebSocketServer
import org.java_websocket.WebSocket

class MyWebSocket {
    private lateinit var websocket: WebSocket
    private val TAG = "MyWebSocket"

    init {
        //根据播放器角色创建对应的websocket类
        when (VideoPlayerParams.getInstance().serverIp) {
            null -> {
                MyWebSocketServer().start()
            }

            else -> {
                MyWebSocketClient(VideoPlayerParams.getInstance().serverIp, TAG).connect()
            }
        }
    }
}