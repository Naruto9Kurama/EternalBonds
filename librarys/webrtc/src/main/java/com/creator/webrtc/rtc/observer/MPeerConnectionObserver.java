package com.creator.webrtc.rtc.observer;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

public class MPeerConnectionObserver implements PeerConnection.Observer {

    private String TAG="MSdpObserver";
    public MPeerConnectionObserver(String tag,Class clazz){
        TAG=clazz.getSimpleName()+"-"+tag;
    }
    public MPeerConnectionObserver(Class clazz) {
        TAG=clazz.getSimpleName();
    }
    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.d(TAG,"onSignalingChange");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

        Log.d(TAG,"onIceConnectionChange --- "+iceConnectionState.name());
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        Log.d(TAG,"onIceConnectionReceivingChange");

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d(TAG,"onIceGatheringChange");

    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Log.d(TAG,"onIceCandidate");

    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        Log.d(TAG,"onIceCandidatesRemoved");

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        Log.d(TAG,"onAddStream");

    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.d(TAG,"onRemoveStream");

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.d(TAG,"onDataChannel");

    }

    @Override
    public void onRenegotiationNeeded() {
        Log.d(TAG,"onRenegotiationNeeded");

    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        Log.d(TAG,"onAddTrack");

    }
}
