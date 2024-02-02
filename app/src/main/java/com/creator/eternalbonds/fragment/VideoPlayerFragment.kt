 package com.creator.eternalbonds.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.creator.common.bean.player.VideoItemBean
import com.creator.common.bean.player.VideoPlayerDataBean
import com.creator.common.fragment.BaseFragment
import com.creator.eternalbonds.databinding.FragmentVideoPlayerBinding

 @OptIn(UnstableApi::class)
class VideoPlayerFragment(val playerLoadProgress:PlayerLoadProgress?=null) :
    BaseFragment<FragmentVideoPlayerBinding>() {

     lateinit var exoPlayer: ExoPlayer


    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = exoPlayer
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerLoadProgress?.onLoadingFinish()
    }



    fun setMediaSource(videoItemBean: VideoItemBean,  isPlayWhenReady: Boolean = false){
        // 创建一个MediaItem对象，表示要播放的媒体文件
        val mediaItem = MediaItem.fromUri(videoItemBean.uri)
        // 设置数据源工厂
        val dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), "com.creator.eternalbonds")
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



     interface PlayerLoadProgress{
         /**
          * 播放器加载完成回调
          */
         fun onLoadingFinish();
     }

}