package com.creator.eternalbonds.listener

import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.creator.common.bean.player.VideoPlayerDataBean
import com.creator.common.bean.transmit.WebSocketMessage
import com.creator.common.utils.LogUtil
import com.creator.exoplayer.utils.ExoPlayerController
import com.creator.websocket.VideoWebSocket

@UnstableApi
class PlayerListener(val exoPlayerController: ExoPlayerController,val videoPlayerDataBean:VideoPlayerDataBean,val webSocket: VideoWebSocket) : Player.Listener {
    private val TAG = "PlayerListener"

    //播放状态变化监听
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        when (playbackState) {
            // 处于缓冲状态
            Player.STATE_BUFFERING -> {
            }
            // 已准备好播放
            Player.STATE_READY -> {
                // 播放器已准备好
                if (playWhenReady) {
                    // 播放中
                } else {
                    // 暂停
                }
            }
            // 播放已结束
            Player.STATE_ENDED -> {

            }
            // 播放器处于空闲状态p
            Player.STATE_IDLE -> {

            }

        }
    }

    //进度条变化监听
    override fun onPositionDiscontinuity(reason: Int) {
        super.onPositionDiscontinuity(reason)
        LogUtil.d(TAG, "进度条变化")
        if (!exoPlayerController.isSeekTo) {
            videoPlayerDataBean.videoItemBeanList[videoPlayerDataBean.currentIndex].currentPosition= reason.toLong()
            val webSocketMessage = WebSocketMessage(videoPlayerDataBean)
            val message = webSocketMessage.toString()
            val toClass = WebSocketMessage.toClass< VideoPlayerDataBean>(message)
            val message1 = toClass?.message
            webSocket.send(message)
        }
        exoPlayerController.isSeekTo = false
    }


}