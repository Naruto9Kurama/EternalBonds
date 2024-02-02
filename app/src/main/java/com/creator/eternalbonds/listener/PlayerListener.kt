package com.creator.eternalbonds.listener

import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.creator.common.utils.LogUtil

@UnstableApi
class PlayerListener() : Player.Listener {
    private val TAG="PlayerListener"

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
            // 播放器处于空闲状态
            Player.STATE_IDLE -> {

            }

        }
    }

    //进度条变化监听
    override fun onPositionDiscontinuity(reason: Int) {
        super.onPositionDiscontinuity(reason)
        LogUtil.d(TAG, "进度条变化")
    }



}