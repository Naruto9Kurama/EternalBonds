package com.creator.webrtc.rtc;

import android.content.Context;
import android.util.Log;
import com.creator.webrtc.bean.SessionDescriptionBean;
import com.creator.webrtc.constants.RTCConstans;
import com.creator.webrtc.constants.WebSocketConstans;
import com.creator.webrtc.rtc.observer.MPeerConnectionObserver;
import com.creator.webrtc.rtc.observer.MSdpObserver;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.webrtc.DataChannel;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * WebRTC发起端
 */
public class WebRTCInitiator extends RTCWebSocketClient {

    private static final String TAG = "WebRTCInitiator";
    private static int connectNum = 1;
    private PeerConnectionFactory factory;
    private PeerConnection peerConnection;
    Context context;

    public WebRTCInitiator(Context context, String token) throws URISyntaxException {
        /*super(new URI(WebSocketConstans.webSocketUrl + token + "-" + connectNum++ + WebSocketConstans.webSocketUrlInitiatorSuffix),
                WebRTCInitiator.class.getSimpleName());*/
        super(new URI(WebSocketConstans.webSocketUrl + "123"+WebSocketConstans.webSocketUrlInitiatorSuffix),
                WebRTCInitiator.class.getSimpleName());

        this.context = context;
        init();
    }


    /**
     * 初始化PeerConnectionFactory
     */
    private void init() {
        // 初始化 PeerConnectionFactory
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(context)
                        .setEnableInternalTracer(true)
                        .createInitializationOptions();

        PeerConnectionFactory.initialize(initializationOptions);
        // 创建 PeerConnectionFactory
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        factory = PeerConnectionFactory.builder()
                .setOptions(options)
                .createPeerConnectionFactory();
//        Logging.enableLogToDebugOutput(Logging.Severity.LS_VERBOSE);
    }


    /**
     * 创建PeerConnection
     */
    public void createPeerConnection(List<PeerConnection.IceServer> iceServers) {
        // 创建 PeerConnection
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers != null && !iceServers.isEmpty() ? iceServers : RTCConstans.iceServers);
        peerConnection = factory.createPeerConnection(rtcConfig, new MPeerConnectionObserver(WebRTCInitiator.class));
        peerConnection.createDataChannel(RTCConstans.dataChannelDog, new DataChannel.Init());
    }


    /**
     * 创建 Offer
     */
    public void createOffer() {
        peerConnection.createOffer(new MSdpObserver(WebRTCInitiator.class) {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                setLocalDescription(sessionDescription);
            }

            @Override
            public void onCreateFailure(String s) {
                super.onCreateFailure(s);
            }
        }, new MediaConstraints());

    }

    public String checkConnectState(){
        Log.d(TAG, peerConnection.connectionState().name());
        return peerConnection.connectionState().name();
    }

    /**
     * 设置本地Description
     *
     * @param sessionDescription
     */
    public void setLocalDescription(SessionDescription sessionDescription) {
        peerConnection.setLocalDescription(new MSdpObserver(WebRTCInitiator.class) {
            @Override
            public void onSetSuccess() {
                super.onSetSuccess();
                String jsonString = (new Gson()).toJson(sessionDescription);
                Log.d(TAG, jsonString);
                SessionDescriptionBean sessionDescriptionBean = new SessionDescriptionBean("456"+WebSocketConstans.webSocketUrlReceiverSuffix, jsonString);
                send(sessionDescriptionBean.toString());
            }

            @Override
            public void onSetFailure(String s) {
                super.onSetFailure(s);
            }
        }, sessionDescription);
    }



    /**
     * 设置answer
     *
     * @param sessionDescription
     */
    public void setAnswer(SessionDescription sessionDescription) {
        peerConnection.setRemoteDescription(new MSdpObserver(WebRTCInitiator.class) {
            @Override
            public void onSetSuccess() {
                super.onSetSuccess();
                Log.d(TAG, "SetSuccess");

            }

            @Override
            public void onSetFailure(String s) {
                super.onSetFailure(s);
            }
        }, sessionDescription);
    }



    public void close() {
        // 关闭 PeerConnection 和 PeerConnectionFactory
        if (peerConnection != null) {
            peerConnection.dispose();
            peerConnection = null;
        }

        if (factory != null) {
            factory.dispose();
            factory = null;
        }
    }

    /*****************   WebSocket  Override Start*******************************/
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        super.onOpen(serverHandshake);

    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);
        SessionDescriptionBean sessionDescriptionBean = SessionDescriptionBean.toClass(message);
        setAnswer(new Gson().fromJson(sessionDescriptionBean.getSessionDescriptionStr(), SessionDescription.class));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        super.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        super.onError(ex);
    }
    /*****************   WebSocket  Override End*******************************/


}

