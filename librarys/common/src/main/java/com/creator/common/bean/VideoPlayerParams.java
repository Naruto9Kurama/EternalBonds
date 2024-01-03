package com.creator.common.bean;

import com.creator.common.utils.IPUtil;

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
    private List<String> ip = new ArrayList<>(); //ip地址

    private List<VideoItemBean> videoItemBeanList = new ArrayList<>();//视频item列表

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
        this.myIp = myIp;
    }

    public List<String> getIp() {
        return ip;
    }


    public List<VideoItemBean> getVideoItemBeanList() {
        return videoItemBeanList;
    }

    public void setVideoItemBeanList(List<VideoItemBean> videoItemBeanList) {
        this.videoItemBeanList = videoItemBeanList;
    }
}
