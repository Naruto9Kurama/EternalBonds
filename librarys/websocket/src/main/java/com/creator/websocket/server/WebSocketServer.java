package com.creator.websocket.server;

import android.util.Log;

import com.creator.common.Constants;
import com.creator.common.bean.VideoTransmitBean;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;


public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    public WebSocketServer() {
        this(new InetSocketAddress(Constants.WebSocket.PORT));
    }

    public WebSocketServer(InetSocketAddress address) {
        super(address);
    }

    public WebSocketServer(InetSocketAddress address, int decodercount) {
        super(address, decodercount);
    }

    public WebSocketServer(InetSocketAddress address, List<Draft> drafts) {
        super(address, drafts);
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        super(address, decodercount, drafts);
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        super(address, decodercount, drafts, connectionscontainer);
    }

    private String TAG = this.getClass().getSimpleName();


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG,"onOpen");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG,"onClose");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG,"onMessage:::"+message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.e(TAG,"onError:::"+ex.getMessage(),ex);

    }

    @Override
    public void onStart() {
        Log.d(TAG,"onStart");

    }
}
