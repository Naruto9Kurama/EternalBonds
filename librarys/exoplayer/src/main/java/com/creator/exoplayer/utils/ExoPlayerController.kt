package com.creator.exoplayer.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.creator.common.utils.CoroutineUtils

@OptIn(UnstableApi::class)
class ExoPlayerController(val context: Context) {

    private var exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
     var isSeekTo = false

    /**
     * 开始播放
     */
    fun play() {
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
    fun setMediaSource(
        mediaSource: MediaSource,
        resetPosition: Boolean = true,
        isPlayWhenReady: Boolean = false
    ) {
        exoPlayer.setMediaSource(
            mediaSource,
            resetPosition
        )
        // 准备播放
        exoPlayer.prepare()
        if (isPlayWhenReady) exoPlayer.playWhenReady = true;

    }

    fun setMediaSource(uri: String, isPlayWhenReady: Boolean = false) {
        // 创建一个MediaItem对象，表示要播放的媒体文件
        val mediaItem = MediaItem.fromUri(uri)
        // 设置数据源工厂
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "com.creator.eternalbonds")
        )
        // 创建一个ProgressiveMediaSource，用于播放常规的媒体文件（例如MP4）
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        // 设置要播放的媒体项到ExoPlayer
        exoPlayer.setMediaItem(mediaItem)
        // 设置媒体源工厂到ExoPlayer
        setMediaSource(mediaSource, isPlayWhenReady = isPlayWhenReady)
    }
}