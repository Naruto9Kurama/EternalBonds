package com.creator.common.bean;

import com.creator.common.enums.Enums;
import com.google.gson.Gson;

public class WebSocketMessageBean {

    public WebSocketMessageBean() {
    }

    public WebSocketMessageBean(Enums.MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    private Enums.MessageType messageType;
    private String message;

    public Enums.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(Enums.MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static WebSocketMessageBean toClass(String str) {
        return new Gson().fromJson(str, WebSocketMessageBean.class);

    }
}
