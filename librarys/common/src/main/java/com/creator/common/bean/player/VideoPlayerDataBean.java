package com.creator.common.bean.player;


import com.creator.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频播放器数据类
 */
public class VideoPlayerDataBean {

    private String serverIp= Constants.Data.Ip.myIp;
    private List<VideoItemBean> videoItemBeanList=new ArrayList<>();//视频列表
    private int currentIndex;//当前播放列表位置下标

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public List<VideoItemBean> getVideoItemBeanList() {
        return videoItemBeanList;
    }

    public void setVideoItemBeanList(List<VideoItemBean> videoItemBeanList) {
        this.videoItemBeanList = videoItemBeanList;
    }

    public int getCurrentIndex(){
        return videoItemBeanList.size()-1;
    }
}
