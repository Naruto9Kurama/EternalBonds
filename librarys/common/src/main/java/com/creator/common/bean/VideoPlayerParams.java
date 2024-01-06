package com.creator.common.bean;

import com.creator.common.Constants;
import com.creator.common.enums.Enums;
import com.creator.common.utils.IPUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VideoPlayerParams {
    private VideoPlayerParams() {
    }

    private static VideoPlayerParams videoPlayerParams = null;

    public static VideoPlayerParams getInstance() {
        if (videoPlayerParams == null) {
            videoPlayerParams = new VideoPlayerParams();
        }
        return videoPlayerParams;
    }

    private Set<String> myIps = new HashSet<>(); //我的ip地址
    private String serverIp;//房间服务器ip
    private Enums.PlayerRole playerRole;
    private List<String> ip = new ArrayList<>(); //ip地址


    private List<VideoItemBean> videoItemBeanList = new ArrayList<>();//视频item列表

    public String getServerIp() {
        return serverIp;
    }

    public boolean setServerIp(String serverIp) {
        if (!IPUtil.INSTANCE.ipIsReachable(serverIp)) {//判断是否是有效ip
            return false;
        }
        if (IPUtil.INSTANCE.isIpv6(serverIp)) {
            serverIp = "[" + serverIp + "]";
        }
        this.serverIp = serverIp;
        return true;
    }

    public Enums.PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(Enums.PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public String getWebSocketServerIp() {
        return "ws://" + serverIp + ":" + Constants.WebSocket.PORT;
    }

    public void setIp(List<String> ip) {
        this.ip = ip;
    }

    public void setMyIps(Set<String> myIps) {
        this.myIps = myIps;
    }

    public String getMyIp() {
        for (String ip : myIps) {
            if (IPUtil.INSTANCE.isPublicIPv6(ip)) {
                return ip;
            }
        }
        return myIps.toArray()[0].toString();
    }

    public Set<String> getMyIps() {
        return myIps;
    }

    public void addMyIp(String myIp) {
        myIp = myIp.trim().replaceAll("\\n$", "");
        /*if (IPUtil.INSTANCE.isIpv6(myIp)) {
            myIp = "[" + myIp + "]";
        }*/
        this.myIps.add(myIp);
    }

    public List<String> getIp() {
        return ip;
    }


    public List<VideoItemBean> getVideoItemBeanList() {
        return videoItemBeanList;
    }

    public String getCurrentVideoUri() {
        return videoItemBeanList.get(videoItemBeanList.size() - 1).getUri();
    }

    public VideoItemBean getCurrentVideoItemBean() {
        return videoItemBeanList.get(0);
    }

    public void setVideoItemBeanList(List<VideoItemBean> videoItemBeanList) {
        this.videoItemBeanList = videoItemBeanList;
    }

    public VideoPlayerParams toClass(String str) {
        VideoPlayerParams videoPlayerParams = new Gson().fromJson(str, VideoPlayerParams.class);
        videoPlayerParams.myIps = VideoPlayerParams.getInstance().myIps;
        this.videoPlayerParams = videoPlayerParams;
        return this.videoPlayerParams;
    }

    @Override
    public String toString() {
        return new Gson().toJson(videoPlayerParams);
    }
}
