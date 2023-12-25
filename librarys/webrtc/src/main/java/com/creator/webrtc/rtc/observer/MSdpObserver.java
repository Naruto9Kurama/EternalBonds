package com.creator.webrtc.rtc.observer;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class MSdpObserver implements SdpObserver {

    private String TAG="MSdpObserver";
    public MSdpObserver(String tag,Class clazz){
        TAG=clazz.getSimpleName()+"-"+tag;
    }
    public MSdpObserver(Class clazz) {
        TAG=clazz.getSimpleName();
    }
    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Log.d(TAG,"onCreateSuccess");

    }

    @Override
    public void onSetSuccess() {
        Log.d(TAG,"onSetSuccess");

    }

    @Override
    public void onCreateFailure(String s) {
        Log.d(TAG,"onCreateFailure");

    }

    @Override
    public void onSetFailure(String s) {
        Log.d(TAG,"onSetFailure");

    }
}
