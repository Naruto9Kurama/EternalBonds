package com.creator.eternalbonds

import com.creator.common.Constants
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.junit.Test

import org.junit.Assert.*
import java.net.URI

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        try {
            val webSocketClient =
                object : WebSocketClient(URI("ws://120.32.251.49:${Constants.WebSocket.PORT}")){
                    override fun onOpen(handshakedata: ServerHandshake?) {
                        println("onOpen")
                    }

                    override fun onMessage(message: String?) {
                        println("onMessage")
                    }

                    override fun onClose(code: Int, reason: String?, remote: Boolean) {
                        println("onClose")
                    }

                    override fun onError(ex: java.lang.Exception?) {
                        println("onError")
                    }

                }
            while (true){

            }

        } catch (e: Exception) {

        }

    }
}