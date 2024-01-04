package com.creator.exoplayer.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.Constants
import com.creator.common.bean.VideoItemBean
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.bean.VideoTransmitBean
import com.creator.common.enums.Enums
import com.creator.common.utils.FileUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.ToastUtil
import com.creator.exoplayer.databinding.FragmentVideoPageBinding
import com.creator.nanohttpd.server.VideoNanoHttpDServer
import com.google.android.exoplayer2.Player
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.net.URI


/**
 * A simple [Fragment] subclass.
 * Use the [VideoPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPageFragment : Fragment() {

    private val TAG = "VideoPageFragment"
    private lateinit var binding: FragmentVideoPageBinding
    private lateinit var localFilesRadioButton: RadioButton
    private lateinit var httpRadioButton: RadioButton
    private lateinit var screenCastingRadioButton: RadioButton
    private lateinit var localFileUri: String
    private lateinit var myWebSocket: MyWebSocket
    private var videoPlayerParams: VideoPlayerParams = VideoPlayerParams.getInstance()
    lateinit var player: VideoPlayerFragment

    private var isSeekTo = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoPageBinding.inflate(layoutInflater)
        player = VideoPlayerFragment.newInstance()

        // 使用 FragmentManager 启动 Fragment
        childFragmentManager.beginTransaction().replace(binding.playerView.id, player).commit()
        init()
        return binding.root
    }

    /**
     * 初始化对象
     */
    private fun init() {
        //初始化websocket
        myWebSocket = MyWebSocket()
        //本地文件RadioButton
        localFilesRadioButton = binding.localFilesRadioButton
        //Http RadioButton
        httpRadioButton = binding.httpRadioButton
        //投屏RadioButton
        screenCastingRadioButton = binding.screenCastingRadioButton
        addListener()
    }

    private fun addListener() {
        //播放按钮
        binding.startPlayer.setOnClickListener {
            val videoItemBean = VideoItemBean()
            when (binding.playbackRadioGroup.checkedRadioButtonId) {
                localFilesRadioButton.id -> {
                    videoItemBean.setLocalUri(localFileUri)
                    videoItemBean.playbackSource = Enums.PlaybackSource.LOCAL_FILES
                }

                httpRadioButton.id -> {
                    videoItemBean.uri = binding.httpEditText.text.toString()
                    videoItemBean.playbackSource = Enums.PlaybackSource.HTTP
                }

                screenCastingRadioButton.id -> {
                }

                else -> {
                }
            }
            videoItemBean.ip = videoPlayerParams.myIp
            videoPlayerParams.videoItemBeanList.add(videoItemBean)

            player.startPlay(videoItemBean.uri) {
                addPlayerListener()
            }
            startNano()

        }
        //选择文件按钮
        binding.chooseFileBtn.setOnClickListener {
            // 启动文件选择器
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, Enums.FileRequestCode.VIDEO.ordinal)
        }

    }

    fun addPlayerListener() {
        //视频进度条变化监听
        player.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                LogUtil.d(TAG, "进度条变化")
                if (!isSeekTo) {
                    val currentPosition = player.getCurrentPosition()
                    val videoTransmitBean = VideoTransmitBean()
                    videoPlayerParams.currentVideoItemBean.currentPosition = currentPosition
                    videoTransmitBean.uri = videoPlayerParams.currentVideoUri
                    LogUtil.d(TAG, "当前时间:::$currentPosition")
                    //发送当前变更位置
                    myWebSocket.send(videoPlayerParams)
                }
                isSeekTo = false
            }
        })
    }

    fun seekTo(l: Long) {
        isSeekTo = true
        player.seekTo(l)
    }

    fun startNano() {
        val videoNanoHttpDServer =
            VideoNanoHttpDServer(
                uri = videoPlayerParams.currentVideoUri,
                context = context
            )
        videoNanoHttpDServer.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Enums.FileRequestCode.VIDEO.ordinal && resultCode == AppCompatActivity.RESULT_OK) {
            // 处理选择的视频文件
            val videoAddress = data?.data

            localFileUri = videoAddress.toString()
            val filePathFromUri =
                FileUtil.getRealPathFromUri(requireContext(), Uri.parse(videoAddress.toString()))
            binding.chooseFilePathText.text = filePathFromUri
        }
    }


    inner class MyWebSocket {
        private var websockets = ArrayList<WebSocket>()

        init {
            //根据播放器角色创建对应的websocket类
            when (VideoPlayerParams.getInstance().serverIp) {
                null -> {
                    ExoPlayerWebSocketServer().start()
                }

                else -> {
                    ExoPlayerWebSocketClient().connect()
                }
            }
        }

        inner class ExoPlayerWebSocketClient constructor(uri: String = videoPlayerParams.webSocketServerIp) :
            org.java_websocket.client.WebSocketClient(URI(uri)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                websockets.add(connection)
                ToastUtil.show(context, "连接成功")
            }

            override fun onMessage(message: String?) {
                LogUtil.d(TAG, message.toString())
                updateVideo(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                LogUtil.e(TAG, "onError$reason")
            }

            override fun onError(ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }
        }


        inner class ExoPlayerWebSocketServer constructor(port: Int = Constants.WebSocket.PORT) :
            WebSocketServer(InetSocketAddress(port)) {
            override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
                websockets.add(conn)

//                val videoTransmitBean = VideoTransmitBean()
//                videoTransmitBean.uri = videoPlayerParams.currentVideoUri
//                videoTransmitBean.currentPosition = player.getCurrentPosition()
                send(VideoPlayerParams.getInstance())

            }

            override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
            }

            override fun onMessage(conn: WebSocket?, message: String?) {
                LogUtil.d(TAG, "onMessage:::$message")
                updateVideo(message)
            }

            override fun onError(conn: WebSocket?, ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }

            override fun onStart() {
                LogUtil.d(TAG, "onStart")
                ToastUtil.show(requireContext(), "WebSocket服务启动成功")

            }
        }

        /**
         * 通过websocket接收到的消息更新视频数据
         */
        fun updateVideo(message: String?) {
            val videoPlayerParams = VideoPlayerParams.getInstance().toClass(message)
            LogUtil.d(TAG, videoPlayerParams.toString())
            if (videoPlayerParams != null) {
                player.startPlay(videoPlayerParams.currentVideoUri)
                if (videoPlayerParams.currentVideoItemBean.currentPosition != null) {
                    player.seekTo(videoPlayerParams.currentVideoItemBean.currentPosition)
                }
            }
        }

        fun send(videoPlayerParams: VideoPlayerParams) {

            websockets.forEach { websocket ->
                try {
                    websocket.send(videoPlayerParams.toString())
                } catch (e: Exception) {
                    LogUtil.e(TAG, "send失败：${videoPlayerParams.toString()}\n" + e.message, e)
                }
            }

        }
        /*fun send(videoTransmitBean: VideoTransmitBean) {

            websockets.forEach { websocket ->
                try {
                    websocket.send(videoTransmitBean.toString())
                } catch (e: Exception) {
                    LogUtil.e(TAG, "send失败：${videoTransmitBean.toString()}\n" + e.message, e)
                }
            }

        }*/


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoPageFragment.
         */
        @JvmStatic
        fun newInstance() =
            VideoPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}