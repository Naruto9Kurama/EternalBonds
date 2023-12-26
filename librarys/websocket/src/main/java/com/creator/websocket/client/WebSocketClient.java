package com.creator.websocket.client;

import android.util.Log;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    String TAG = "RTCWebSocketClient";

    public WebSocketClient(URI serverUri, String tag) {
        super(serverUri);
        TAG=tag;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Log.d(TAG, "onOpen");
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, "onMessage:::"+message);


    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "onClose");

    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "onError:::" + ex.getMessage(),ex);

    }


}

