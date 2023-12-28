package com.creator.exoplayer.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creator.common.Constants
import com.creator.common.enums.Enums
import com.creator.common.utils.IPUtil
import com.creator.common.utils.LogUtil
import com.creator.exoplayer.databinding.FragmentVideoPlayerBinding
import com.creator.exoplayer.player.ExoPlayerSingleton
import com.creator.nanohttpd.server.VideoNanoHttpDServer
import java.net.URI


private const val VIDEO_ROLE_ENUM_KEY = "VideoRoleEnumKey"
private const val SERVER_IP_KEY = "ServerIPKey"
private const val URI_KEY = "uriKey"

class VideoPlayerFragment : Fragment() {
    private val TAG = "VideoPlayerFragment"

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private var role = Enums.VideoRole.Server
    private lateinit var serverIp: String
    private lateinit var webSocketUri: String
    private lateinit var uri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            role = it.getSerializable(VIDEO_ROLE_ENUM_KEY) as Enums.VideoRole
            serverIp = it.getString(SERVER_IP_KEY)!!
//            uri = it.getString(URI_KEY)!!
        }
        //判断serverIp是否是公网地址
        if (IPUtil.isPublicIP(serverIp)) {
            LogUtil.d(TAG,"当前地址为公网地址:::${serverIp}")
            webSocketUri = "ws://" + if (IPUtil.isIpv6(serverIp)) {
                "[$serverIp]"
            } else {
                serverIp
            } + ":${Constants.WebSocket.PORT}"
        }else{
            LogUtil.d(TAG,"当前地址为内网地址:::${serverIp}")
            webSocketUri = "ws://" + if (IPUtil.isIpv6(serverIp)) {
                "[$serverIp]"
            } else {
                serverIp
            } + ":${Constants.WebSocket.PORT}"
        }

        Log.d(TAG, role.name)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        when (role) {
            Enums.VideoRole.Server -> {
                binding.playerView.player =
                    ExoPlayerSingleton.getExoPlayer(requireContext(), Enums.VideoRole.Server)

                val videoNanoHttpDServer = VideoNanoHttpDServer(videoUri = URI.create(uri), context = context)
                videoNanoHttpDServer.start()

                ExoPlayerSingleton.setSource(
                    "http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4",
                    requireContext()
                )
                ExoPlayerSingleton.play()
            }

            Enums.VideoRole.Client -> {
                binding.playerView.player = ExoPlayerSingleton.getExoPlayer(
                    requireContext(),
                    Enums.VideoRole.Client,
                    webSocketUri
                )

            }
        }
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Enums.VideoRole, ip: String,uri: String?) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    //接受传递的参数
                    putSerializable(VIDEO_ROLE_ENUM_KEY, param1)
                    putString(URI_KEY, uri)
                    putString(SERVER_IP_KEY, ip)
                }
            }
    }
}