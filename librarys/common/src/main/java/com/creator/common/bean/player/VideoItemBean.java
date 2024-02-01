package com.creator.common.bean.player;

import com.creator.common.Constants;
import com.creator.common.bean.BaseBean;
import com.creator.common.bean.VideoPlayerParams;
import com.creator.common.enums.Enums;
import com.creator.common.utils.LogUtil;

/**
 * 视频列表的每条视频信息类
 */
public class VideoItemBean extends BaseBean {
    private Enums.PlaybackSource playbackSource; //播放源
    private String uri;//视频的uri
    private String ip;//视频归属哪个ip地址


    private Long currentPosition;//视频播放位置
    private int playerState;//播放状态 缓冲中:Player.STATE_BUFFERING 准备好播放:Player.STATE_READY 播放已结束:Player.STATE_ENDED 处于空闲状态:Player.STATE_IDLE
    private String localUri;//如果播放器角色是服务端，且播放源为本地文件，localUri存储为本地文件的路径

    public Long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
        this.uri = "http://" + VideoPlayerParams.getInstance().getMyIp() + ":" + Constants.NanoHttpd.PORT + "/video";
//        this.uri = "http://" + VideoPlayerParams.getInstance().getMyIp() + ":" + Constants.NanoHttpd.PORT+"/video";
    }

    public Enums.PlayerRole getPlayerRole() {
        if (VideoPlayerParams.getInstance().getMyIp().equals(ip)) {
            return Enums.PlayerRole.Server;
        } else {
            return Enums.PlayerRole.Client;
        }
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public Enums.PlaybackSource getPlaybackSource() {
        return playbackSource;
    }

    public void setPlaybackSource(Enums.PlaybackSource playbackSource) {
        this.playbackSource = playbackSource;
    }

    public String getUri() {

        if (playbackSource == Enums.PlaybackSource.LOCAL_FILES) {
            switch (getPlayerRole()) {
                case Server:
                    return localUri;
                case Client:
                    String uri = "http://" + VideoPlayerParams.getInstance().getServerIp() + ":" + Constants.NanoHttpd.PORT+"/video";
                    LogUtil.INSTANCE.d(TAG,uri,null);
                    return uri;
            }
        }
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
