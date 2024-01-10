package com.creator.exoplayer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creator.common.MainThreadExecutor
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.fragment.BaseFragment
import com.creator.common.utils.URIUtils
import com.creator.exoplayer.databinding.FragmentVideoPlayerBinding
import com.creator.exoplayer.player.ExoPlayerSingleton
import com.creator.nanohttpd.server.VideoNanoHttpDServer
import com.google.android.exoplayer2.Player.Listener


class VideoPlayerFragment : BaseFragment<FragmentVideoPlayerBinding>() {
    private val TAG = "VideoPlayerFragment"
    override fun init() {
    }

    override fun addListener() {
    }
    fun getCurrentPosition(): Long {
        return ExoPlayerSingleton.getCurrentPosition()
    }

    fun startPlay(uri: String, block: (() -> Unit)? = null) {
        MainThreadExecutor.runOnUiThread {
            if (binding.playerView.player == null) {
                binding.playerView.player = ExoPlayerSingleton.getExoPlayer(requireContext())
            }
            ExoPlayerSingleton.setSource(uri, requireContext(), true)
            ExoPlayerSingleton.play()
            block?.invoke()
        }
    }



    fun addListener(listener: Listener) {
        ExoPlayerSingleton.addListener(listener)
    }

    fun removeListener(listener: Listener) {
        ExoPlayerSingleton.removeListener(listener)
    }

    fun seekTo(l: Long) {
        ExoPlayerSingleton.seekTo(l)
    }

    fun pause() {
        ExoPlayerSingleton.pause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    //接受传递的参数
                }
            }

    }


}