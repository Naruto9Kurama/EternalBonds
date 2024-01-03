package com.creator.common.bean;

import android.net.Uri;

import com.creator.common.enums.Enums;

public class VideoItemBean {
    private Enums.PlayerRole playerRole; //播放器角色
    private Enums.PlaybackSource playbackSource; //播放源
    private String uri;//视频的uri
    private String ip;//视频归属哪个ip地址


    public Enums.PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(Enums.PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public Enums.PlaybackSource getPlaybackSource() {
        return playbackSource;
    }

    public void setPlaybackSource(Enums.PlaybackSource playbackSource) {
        this.playbackSource = playbackSource;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
