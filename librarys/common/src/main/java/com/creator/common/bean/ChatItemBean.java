package com.creator.common.bean;

import java.util.Date;

public class ChatItemBean {
    public ChatItemBean() {
    }

    public ChatItemBean(String sender, String message, Date date) {
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    private String sender;//发送者
    private String message;//发送消息
    private Date date;//发送时间

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
