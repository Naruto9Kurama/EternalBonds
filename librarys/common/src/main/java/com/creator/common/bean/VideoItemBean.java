package com.creator.common.bean;

import android.net.Uri;

import com.creator.common.Constants;
import com.creator.common.enums.Enums;

public class VideoItemBean {
    //    private Enums.PlayerRole playerRole; //播放器角色
    private Enums.PlaybackSource playbackSource; //播放源
    private String uri;//视频的uri
    private String ip;//视频归属哪个ip地址
//    private String myIp; //我的ip地址

    private Long currentPosition;//视频播放位置
    private String localUri;//如果播放器角色是服务端，且播放源为本地文件，localUri存储为本地文件的路径


    public Long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
        this.uri = "http://" + VideoPlayerParams.getInstance().getMyIp() + ":" + Constants.NanoHttpd.PORT+"/video";
    }

    public Enums.PlayerRole getPlayerRole() {
        if (VideoPlayerParams.getInstance().getMyIp().equals(ip)) {
            return Enums.PlayerRole.Server;
        } else {
            return Enums.PlayerRole.Client;
        }
    }

    public Enums.PlaybackSource getPlaybackSource() {
        return playbackSource;
    }

    public void setPlaybackSource(Enums.PlaybackSource playbackSource) {
        this.playbackSource = playbackSource;
    }

    public String getUri() {
        if (getPlayerRole() == Enums.PlayerRole.Server && playbackSource == Enums.PlaybackSource.LOCAL_FILES) {
            return localUri;
        } else {
            return uri;
        }
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
