package com.creator.remote_controller.activity

import com.creator.common.Constants
import com.creator.common.activity.BaseActivity
import com.creator.common.enums.Enums
import com.creator.common.utils.LogUtil
import com.creator.remote_controller.databinding.ActivityRemoteControllerBinding
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.net.URI

class RemoteControllerActivity : BaseActivity<ActivityRemoteControllerBinding>() {
    lateinit var remoteRole: Enums.RemoteRole
    lateinit var uri: String
    override fun init() {
        MyWebSocket()

        remoteRole = Enums.RemoteRole.valueOf(intent.getStringExtra("remoteRole")!!)
        if (remoteRole == Enums.RemoteRole.Client) {
            uri = intent.getStringExtra("uri")!!
        }
    }

    override fun addListener() {
    }

    inner class MyWebSocket {


        private lateinit var websocket: WebSocket

        init {
            //根据播放器角色创建对应的websocket类
            when (remoteRole) {
                Enums.RemoteRole.Server -> {
                    RemoteWebSocketServer().start()
                }

                Enums.RemoteRole.Client -> {
                    RemoteWebSocketClient().connect()
                }
            }
        }

        inner class RemoteWebSocketClient constructor(uri: String = this@RemoteControllerActivity.uri) :
            org.java_websocket.client.WebSocketClient(URI(uri)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                websocket = this;
            }

            override fun onMessage(message: String?) {
                LogUtil.d(TAG, "RemoteWebSocketClient:::" + message.toString())
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                LogUtil.e(TAG, "onClose$reason")

            }

            override fun onError(ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }
        }


        inner class RemoteWebSocketServer constructor(port: Int = Constants.WebSocket.PORT) :
            WebSocketServer(InetSocketAddress("::", port)) {
            override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
                websocket = conn;
            }

            override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            }

            override fun onMessage(conn: WebSocket?, message: String?) {
                LogUtil.d(TAG, "onMessage:::$message")
            }

            override fun onError(conn: WebSocket?, ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }

            override fun onStart() {
                LogUtil.d(TAG, "onStart")
            }

            override fun start() {
            }
        }
    }

}