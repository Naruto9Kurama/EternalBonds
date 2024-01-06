package com.creator.exoplayer.player

import android.annotation.SuppressLint
import android.content.Context
import com.creator.common.MainThreadExecutor
import com.creator.common.utils.LogUtil
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.Listener
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util


@SuppressLint("StaticFieldLeak")
object ExoPlayerSingleton {
    private const val TAG = "ExoPlayerSingleton"
    private lateinit var exoPlayer: ExoPlayer
    fun getExoPlayer(context: Context, ): ExoPlayer {
        Log.setLogLevel(Log.LOG_LEVEL_ALL)
        exoPlayer = ExoPlayer.Builder(context).build();
        return exoPlayer
    }

    fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }
    fun addListener(listener: Listener){
        exoPlayer.addListener(listener);
    }
    fun removeListener(listener: Listener){
        exoPlayer.removeListener(listener);
    }

    /**
     * 设置播放源
     */
    fun setSource(uri: String, context: Context, isPlayWhenReady: Boolean = false) {
        exoPlayer.clearMediaItems()
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
        exoPlayer.setMediaSource(mediaSource)
        // 准备播放
        exoPlayer.prepare()
        if (isPlayWhenReady) exoPlayer.playWhenReady = true;
    }

    fun play() {
        exoPlayer.play()
    }

    fun seekTo(l: Long) {
        LogUtil.d(TAG, "播放$l")
        MainThreadExecutor.runOnUiThread {
            exoPlayer.seekTo(l)
        }
    }

}
