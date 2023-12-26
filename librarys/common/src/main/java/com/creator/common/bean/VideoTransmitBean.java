package com.creator.common.bean;


import com.google.gson.Gson;

public class VideoTransmitBean {
    String uri;//视频uri位置
    String videoName;//视频名称
    Long currentProgress;//当前播放进度

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static VideoTransmitBean toClass(String str) {
        return new Gson().fromJson(str, VideoTransmitBean.class);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Long getCurrentPosition() {
        return currentProgress;
    }

    public void setCurrentPosition(Long currentProgress) {
        this.currentProgress = currentProgress;
    }
}
