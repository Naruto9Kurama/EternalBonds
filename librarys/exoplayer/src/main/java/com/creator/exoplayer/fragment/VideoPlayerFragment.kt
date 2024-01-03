package com.creator.exoplayer.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.utils.URIUtils
import com.creator.exoplayer.databinding.FragmentVideoPlayerBinding
import com.creator.exoplayer.player.ExoPlayerSingleton
import com.creator.nanohttpd.server.VideoNanoHttpDServer



class VideoPlayerFragment : Fragment() {
    private val TAG = "VideoPlayerFragment"

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private val videoPlayerParams:VideoPlayerParams=VideoPlayerParams.getInstance()

    //    private lateinit var uri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        val sVideoUri=videoPlayerParams.videoItemBeanList[0].uri
        //通过播放器角色，创建播放
        when (videoPlayerParams.videoItemBeanList[0].playerRole) {
            Enums.PlayerRole.Server -> {
                var isLocal = true
                if (!URIUtils.isHttpUri(sVideoUri)) {
                    val videoNanoHttpDServer =
                        VideoNanoHttpDServer(uri = sVideoUri, context = context)
                    videoNanoHttpDServer.start()
                    isLocal = false
                }
                binding.playerView.player =
                    ExoPlayerSingleton.getExoPlayer(
                        requireContext(),
                        Enums.PlayerRole.Server,
                        isLocal = isLocal,
                        serverIp = videoPlayerParams.myIp
                    )

                ExoPlayerSingleton.setSource(
                    sVideoUri,
                    requireContext()
                )
                ExoPlayerSingleton.play()
            }

            Enums.PlayerRole.Client -> {
                binding.playerView.player = ExoPlayerSingleton.getExoPlayer(
                    requireContext(),
                    Enums.PlayerRole.Client,
                    videoPlayerParams.videoItemBeanList[0].ip,
                    isLocal = false
                )
            }

        }
        return binding.root
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