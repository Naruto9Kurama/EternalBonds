package com.creator.common.bean;

import com.creator.common.Constants;
import com.creator.common.enums.Enums;
import com.creator.common.utils.IPUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    private String myIp; //我的ip地址
    private String serverIp;//房间服务器ip
    private List<String> ip = new ArrayList<>(); //ip地址


    private List<VideoItemBean> videoItemBeanList = new ArrayList<>();//视频item列表

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        if (IPUtil.INSTANCE.isIpv6(serverIp)) {
            serverIp = "[" + serverIp + "]";
        }
        this.serverIp = serverIp;
    }

    public String getWebSocketServerIp() {
        return "ws://" + serverIp + ":" + Constants.WebSocket.PORT;
    }

    public void setIp(List<String> ip) {
        this.ip = ip;
    }

    public String getMyIp() {

        if (myIp == null) {
            CompletableFuture<String> future = new CompletableFuture<>();
            IPUtil.INSTANCE.getIpAddress(ip -> {
                myIp = ip.trim().replaceAll("\\n$", "");
                future.complete(ip);
                return null;
            });
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return myIp;
    }

    public void setMyIp(String myIp) {
        if (IPUtil.INSTANCE.isIpv6(myIp)) {
            myIp = "[" + myIp + "]";
        }
        this.myIp = myIp;
    }

    public List<String> getIp() {
        return ip;
    }


    public List<VideoItemBean> getVideoItemBeanList() {
        return videoItemBeanList;
    }

    public String getCurrentVideoUri() {
        return videoItemBeanList.get(0).getUri();
    }
    public VideoItemBean getCurrentVideoItemBean(){
        return videoItemBeanList.get(0);
    }

    public void setVideoItemBeanList(List<VideoItemBean> videoItemBeanList) {
        this.videoItemBeanList = videoItemBeanList;
    }

    public VideoPlayerParams toClass(String str) {
        VideoPlayerParams videoPlayerParams = new Gson().fromJson(str, VideoPlayerParams.class);
        videoPlayerParams.myIp= VideoPlayerParams.getInstance().myIp;
        this.videoPlayerParams=videoPlayerParams;
        return this.videoPlayerParams;
    }

    @Override
    public String toString() {
        return new Gson().toJson(videoPlayerParams);
    }
}
