package com.creator.exoplayer.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creator.common.Constants
import com.creator.common.enums.Enums
import com.creator.exoplayer.databinding.FragmentVideoPlayerBinding
import com.creator.exoplayer.player.ExoPlayerSingleton
import com.creator.nanohttpd.server.VideoServer
import com.google.android.exoplayer2.ui.PlayerControlView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val VIDEO_ROLE_ENUM_KEY = "VideoRoleEnumKey"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : Fragment() {
    private val TAG="VideoPlayerFragment"
    // TODO: Rename and change types of parameters
    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    private var role = Enums.VideoRole.Server
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            role = it.getSerializable(VIDEO_ROLE_ENUM_KEY) as Enums.VideoRole
        }
        Log.d(TAG,role.name)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        when (role) {
            Enums.VideoRole.Server -> {
                binding.playerView.player = ExoPlayerSingleton.getExoPlayer(requireContext(),Enums.VideoRole.Server)
                val videoServer = VideoServer()
                videoServer.start()
                ExoPlayerSingleton.setSource("http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4",requireContext())
                ExoPlayerSingleton.play()
            }

            Enums.VideoRole.Client -> {
                binding.playerView.player = ExoPlayerSingleton.getExoPlayer(requireContext(), Enums.VideoRole.Client,"ws://192.168.2.44:${Constants.WebSocket.PORT}")

            }
        }
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Enums.VideoRole) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(VIDEO_ROLE_ENUM_KEY,param1)
                }
            }
    }
}