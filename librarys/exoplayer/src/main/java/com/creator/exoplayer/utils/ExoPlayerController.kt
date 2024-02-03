package com.creator.exoplayer.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import com.creator.common.MainThreadExecutor
import com.creator.common.utils.CoroutineUtils

@OptIn(UnstableApi::class)
class ExoPlayerController(context: Context) {

    private var exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    private var isSeekTo = false

    /**
     * 开始播放
     */
    fun startPlay() {
        CoroutineUtils.runOnMainThread {
            exoPlayer.play()
        }
    }


    fun getExoPlayer(): ExoPlayer {
        return exoPlayer
    }

    /**
     * 跳转到指定位置播放
     */
    fun seekTo(l: Long) {
        isSeekTo = true
        exoPlayer.seekTo(l)
    }


    /**
     * 设置播放源
     */
    fun setMediaSource(mediaSource: MediaSource,resetPosition:Boolean=true) {
        exoPlayer.setMediaSource(
            mediaSource,
            resetPosition
        )
    }


}