package com.creator.webrtc.bean;

import com.google.gson.Gson;

import org.webrtc.SessionDescription;

public class SessionDescriptionBean {

    public SessionDescriptionBean(String code, String sessionDescription){
        this.code=code;
        this.sessionDescriptionStr =sessionDescription;
    }
    public SessionDescriptionBean(String code, SessionDescription sessionDescription){
        this.code=code;
        this.sessionDescriptionStr =new Gson().toJson(sessionDescription);
    }
    String code;
    String sessionDescriptionStr;

    @Override
    public String toString() {

        return new Gson().toJson(this);
    }

    public String getCode() {
        return code;
    }

    public String getSessionDescriptionStr() {
        return sessionDescriptionStr;
    }

    public static SessionDescriptionBean toClass(String str){
        SessionDescriptionBean sessionDescriptionBean = new Gson().fromJson(str, SessionDescriptionBean.class);
        return sessionDescriptionBean;
    }

    public static SessionDescription toSessionDescription(String str){
        SessionDescriptionBean sessionDescriptionBean = toClass(str);
        return new Gson().fromJson(sessionDescriptionBean.getSessionDescriptionStr(),SessionDescription.class);
    }
}
