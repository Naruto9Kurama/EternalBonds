package com.creator.exoplayer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creator.common.enums.Enums
import com.creator.common.utils.LogUtil
import com.creator.common.utils.URIUtils
import com.creator.exoplayer.databinding.FragmentVideoPlayerBinding
import com.creator.exoplayer.player.ExoPlayerSingleton
import com.creator.nanohttpd.server.VideoNanoHttpDServer
import com.google.gson.Gson
import java.net.URI


private const val VIDEO_ROLE_ENUM_KEY = "VideoRoleEnumKey"
private const val SERVER_IP_KEY = "ServerIPKey"
private const val URI_KEY = "uriKey"

class VideoPlayerFragment : Fragment() {
    private val TAG = "VideoPlayerFragment"

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private var playerRole = Enums.PlayerRole.Server
    private lateinit var cIp: String
    private lateinit var cWebSocketUri: String
//    private lateinit var uri: String
    private lateinit var sVideoUri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //获取参数
            val param = Gson().fromJson<Map<String, Any>>(
                it.getString(ParamKey.PARAM_MAP.name),
                Map::class.java
            )
            LogUtil.d(TAG, param.toString())
            //获取播放器角色

            playerRole = Enums.PlayerRole.valueOf(param[ParamKey.VIDEO_ROLE.name] as String)
            when (playerRole) {
                //服务端
                Enums.PlayerRole.Server -> {
                    sVideoUri = param[ParamKey.VIDEO_URI.name] as String
                }
                //客户端
                Enums.PlayerRole.Client -> {
                    cIp = param[ParamKey.SERVER_IP.name] as String
                }
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)

        //通过播放器角色，创建播放
        when (playerRole) {
            Enums.PlayerRole.Server -> {
                var isLocal=true
                if (!URIUtils.isHttpUri(sVideoUri)){
                    val videoNanoHttpDServer =
                        VideoNanoHttpDServer(videoUri = URI.create(sVideoUri), context = context)
                    videoNanoHttpDServer.start()
                    isLocal=false
                }
                binding.playerView.player =
                    ExoPlayerSingleton.getExoPlayer(requireContext(), Enums.PlayerRole.Server,isLocal=isLocal, serverIp = "192.168.3.3")

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
                    cIp,
                    isLocal=false
                )
            }

        }
        return binding.root
    }

    companion object {

        /* @JvmStatic
         fun newInstance(param1: Enums.VideoRole, ip: String, uri: String?) =
             VideoPlayerFragment().apply {
                 arguments = Bundle().apply {
                     //接受传递的参数
                     putSerializable(VIDEO_ROLE_ENUM_KEY, param1)
                     putString(URI_KEY, uri)
                     putString(SERVER_IP_KEY, ip)
                 }
             }*/

        @JvmStatic
        fun newInstance(param: Map<String, Any?>) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    //接受传递的参数
                    putString(ParamKey.PARAM_MAP.name, Gson().toJson(param))
                }
            }

        enum class ParamKey {
            PARAM_MAP, VIDEO_ROLE, SERVER_IP, VIDEO_URI
        }
    }
}