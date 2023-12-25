package com.creator.webrtc.constants;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.List;

public class RTCConstans {


    public static final List<PeerConnection.IceServer> iceServers =new ArrayList(){{
        add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
    }};

    public static final String dataChannelDog="dog";
}
