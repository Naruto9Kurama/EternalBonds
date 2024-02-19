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
import com.creator.exoplayer.utils.ExoPlayerController


/**
 * 播放器页面
 */
@OptIn(UnstableApi::class)
class VideoPlayerFragment(val playerLoadProgress: PlayerLoadProgress? = null) :
    BaseFragment<FragmentVideoPlayerBinding>() {

    lateinit var exoPlayer: ExoPlayer
    lateinit var exoPlayerController: ExoPlayerController


    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        exoPlayerController = ExoPlayerController(requireContext())
        exoPlayer = exoPlayerController.getExoPlayer()
        binding.playerView.player = exoPlayer
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerLoadProgress?.onLoadingFinish()
    }





    interface PlayerLoadProgress {
        /**
         * 播放器加载完成回调
         */
        fun onLoadingFinish();
    }

}