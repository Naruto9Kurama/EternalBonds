package com.creator.websocket.server;

import com.creator.common.Constants;
import com.creator.common.utils.LogUtil;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;


public class MyWebSocketServer extends org.java_websocket.server.WebSocketServer {

    public MyWebSocketServer() {
        this(new InetSocketAddress(Constants.WebSocket.PORT));
    }

    public MyWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    public MyWebSocketServer(InetSocketAddress address, int decodercount) {
        super(address, decodercount);
    }

    public MyWebSocketServer(InetSocketAddress address, List<Draft> drafts) {
        super(address, drafts);
    }

    public MyWebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        super(address, decodercount, drafts);
    }

    public MyWebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        super(address, decodercount, drafts, connectionscontainer);
    }

    private String TAG = this.getClass().getSimpleName();


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        LogUtil.INSTANCE.d(TAG,"onOpen",null);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LogUtil.INSTANCE.d(TAG,"onClose",null);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        LogUtil.INSTANCE.d(TAG,"onMessage:::"+message,null);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LogUtil.INSTANCE.e(TAG,"onError:::"+ex.getMessage(),ex);

    }

    @Override
    public void onStart() {
        LogUtil.INSTANCE.d(TAG,"onStart",null);

    }
}
