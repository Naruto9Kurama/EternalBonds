package com.creator.webrtc.rtc;

import android.content.Context;
import android.util.Log;

import com.creator.webrtc.bean.SessionDescriptionBean;
import com.creator.webrtc.constants.RTCConstans;
import com.creator.webrtc.constants.WebSocketConstans;
import com.creator.webrtc.rtc.observer.MPeerConnectionObserver;
import com.creator.webrtc.rtc.observer.MSdpObserver;

import org.java_websocket.handshake.ServerHandshake;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * WebRTC接收端
 */
public class WebRTCReceiver extends RTCWebSocketClient {

    private static final String TAG = WebRTCReceiver.class.getSimpleName();

    private PeerConnection peerConnection;
    private static int connectNum = 1;
    Context context;

    public WebRTCReceiver(Context context,String token, List<PeerConnection.IceServer> iceServers) throws URISyntaxException {
        /*super(new URI(WebSocketConstans.webSocketUrl + token + "-" + connectNum++ + WebSocketConstans.webSocketUrlInitiatorSuffix),
                WebRTCReceiver.class.getSimpleName());*/
        super(new URI(WebSocketConstans.webSocketUrl + "456"+WebSocketConstans.webSocketUrlReceiverSuffix),
                WebRTCReceiver.class.getSimpleName());
        this.context = context;
        init(iceServers!=null&&!iceServers.isEmpty() ? iceServers:RTCConstans.iceServers);

    }

    private void init(List<PeerConnection.IceServer> iceServers) {
        // 初始化 PeerConnectionFactory
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(context)
                        .setEnableInternalTracer(true)
                        .createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        // 创建 PeerConnectionFactory
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        PeerConnectionFactory factory = PeerConnectionFactory.builder()
                .setOptions(options)
                .createPeerConnectionFactory();

        // 创建 PeerConnection
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
        peerConnection = factory.createPeerConnection(rtcConfig, new MPeerConnectionObserver(WebRTCReceiver.class));
    }

    /**
     * 接收设置offer
     * @param remoteSdp
     */
    public void handleOffer(SessionDescription remoteSdp) {
//        Log.d(TAG, remoteSdp);

        // 将远程端的 Offer 设置为远程描述
//        SessionDescription offer = new SessionDescription(SessionDescription.Type.OFFER, remoteSdp);
        PeerConnection.SignalingState signalingState = peerConnection.signalingState();
        Log.d(TAG,signalingState.name());
        peerConnection.setRemoteDescription(new MSdpObserver(WebRTCReceiver.class) {

            @Override
            public void onSetSuccess() {
                // 创建 Answer
                Log.d(TAG,"onSetSuccess");
                createAnswer();
            }

            @Override
            public void onSetFailure(String s) {
                Log.d(TAG, "onSetFailure" + s);
            }
        }, remoteSdp);




    }


    /**
     * 创建answer
     */
    public void createAnswer() {
        peerConnection.createAnswer(new MSdpObserver(WebRTCReceiver.class) {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                peerConnection.setLocalDescription(new MSdpObserver(WebRTCReceiver.class), sessionDescription);
                sendAnswerToRemote(sessionDescription);
            }

            @Override
            public void onCreateFailure(String s) {
            }

        }, new MediaConstraints());
    }


    /**
     * 发送answer
     * @param sessionDescription
     */
    private void sendAnswerToRemote(SessionDescription sessionDescription) {
        SessionDescriptionBean sessionDescriptionBean=new SessionDescriptionBean("123"+WebSocketConstans.webSocketUrlInitiatorSuffix,sessionDescription);
        send(sessionDescriptionBean.toString());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        super.onOpen(serverHandshake);
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);


        handleOffer( SessionDescriptionBean.toSessionDescription(message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        super.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        super.onError(ex);
    }
}
