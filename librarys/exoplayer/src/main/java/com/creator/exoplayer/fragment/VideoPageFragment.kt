package com.creator.exoplayer.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.creator.common.Constants
import com.creator.common.bean.FileBean
import com.creator.common.bean.VideoItemBean
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.utils.AppPermissionUtil
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

class VideoPageFragment : Fragment() {

    private val TAG = "VideoPageFragment"
    private lateinit var binding: FragmentVideoPageBinding
    private lateinit var localFilesRadioButton: RadioButton
    private lateinit var httpRadioButton: RadioButton
    private lateinit var screenCastingRadioButton: RadioButton
    private var localFileUri: String? = null
    private var videoPlayerParams: VideoPlayerParams = VideoPlayerParams.getInstance()
    lateinit var player: VideoPlayerFragment
    var videoNanoHttpDServer: VideoNanoHttpDServer? = null

    private val isServer = videoPlayerParams.playerRole == Enums.PlayerRole.Server

    private var isSeekTo = false

    //监听事件
    val listener = object : Player.Listener {
        //播放状态变化监听
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            videoPlayerParams.currentVideoItemBean.playerState = playbackState
            myWebSocket?.send()
            when (playbackState) {
                // 处于缓冲状态
                Player.STATE_BUFFERING -> {
                }
                // 已准备好播放
                Player.STATE_READY -> {

                }
                // 播放已结束
                Player.STATE_ENDED -> {

                }
                // 播放器处于空闲状态
                Player.STATE_IDLE -> {

                }

            }

        }

        //进度条变化监听
        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
            LogUtil.d(TAG, "进度条变化")
            if (!isSeekTo) {
                val currentPosition = player.getCurrentPosition()
                videoPlayerParams.currentVideoItemBean.currentPosition = currentPosition
                LogUtil.d(TAG, "当前时间:::$currentPosition")
                //发送当前变更位置
                myWebSocket?.send()
            }
            isSeekTo = false
        }
    }

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
        if (myWebSocket == null) {
            myWebSocket = MyWebSocket()
        }
        //本地文件RadioButton
        localFilesRadioButton = binding.localFilesRadioButton
        //Http RadioButton
        httpRadioButton = binding.httpRadioButton
        //投屏RadioButton
        screenCastingRadioButton = binding.screenCastingRadioButton
        if (!isServer) {
            //客户端
            binding.ipText.text = videoPlayerParams.serverIp
            binding.btnOpenDrawer.visibility = View.GONE
        } else {
            //服务端
            binding.ipText.text = "  你的ip地址为:\n"
            var i = 1
            videoPlayerParams.myIps.forEach {
                binding.ipText.text =
                    binding.ipText.text.toString() + "  " + i++.toString() + ": " + it + "  \n"
            }
        }
        addListener()

        screenCastingRadioButton.visibility = View.GONE
    }

    private fun addListener() {
        //播放按钮
        binding.startPlayer.setOnClickListener {
            val videoItemBean = VideoItemBean()
            videoPlayerParams.videoItemBeanList.add(videoItemBean)
            videoItemBean.ip = videoPlayerParams.myIp
            when (binding.playbackRadioGroup.checkedRadioButtonId) {
                localFilesRadioButton.id -> {
                    if (localFileUri != null) {
                        videoItemBean.setLocalUri(localFileUri)
                        videoItemBean.playbackSource = Enums.PlaybackSource.LOCAL_FILES
                        startNano()
                    } else {
                        ToastUtil.show(context, "请先选择视频文件")
                        return@setOnClickListener
                    }

                }

                httpRadioButton.id -> {
                    val httpUri = binding.httpEditText.text.toString()
                    if (httpUri.isNotEmpty()) {
                        videoItemBean.uri = httpUri
                        videoItemBean.playbackSource = Enums.PlaybackSource.HTTP
                        closeNano()
                    } else {
                        ToastUtil.show(context, "请先输入视频http地址")
                        return@setOnClickListener
                    }

                }

                screenCastingRadioButton.id -> {
                }

                else -> {
                }
            }

            startPlay() {
                addPlayerListener()
            }

        }
        //选择文件按钮
        binding.chooseFileBtn.setOnClickListener {
            //        PermissionUtils.requestFilePermissions((Activity) context);
            AppPermissionUtil.requestPermissions(
                context,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                object : AppPermissionUtil.OnPermissionListener {
                    override fun onPermissionGranted() {
                        // 启动文件选择器
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "video/*"
                        startActivityForResult(intent, Enums.FileRequestCode.VIDEO.ordinal)
                    }

                    override fun onPermissionDenied() {
                        ToastUtil.show(context, "需要文件权限才能选择文件")
                    }
                })
        }

        //侧边栏
        binding.btnOpenDrawer.setOnClickListener {
            val drawerLayout = binding.drawerLayout
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }

    fun removePlayerListener() {
        player.removeListener(listener)
    }

    fun addPlayerListener() {
        player.addListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
//        removePlayerListener()

    }


    fun seekTo(l: Long) {
        isSeekTo = true
        player.seekTo(l)
    }

    /**
     * 开启Nano
     */
    fun startNano() {
        if (videoNanoHttpDServer == null) {
            videoNanoHttpDServer = VideoNanoHttpDServer(
                uri = videoPlayerParams.currentVideoUri,
                context = context
            )
            videoNanoHttpDServer?.start()
            ToastUtil.show(context, "NanoHttpD已启动")
        } else {
            videoNanoHttpDServer?.setVideoUri(videoPlayerParams.currentVideoUri)
        }
    }

    fun closeNano() {
        if (videoNanoHttpDServer != null) {
            videoNanoHttpDServer?.stop()
            videoNanoHttpDServer = null
        }

    }

    /**
     * 开始播放
     */
    fun startPlay(block: (() -> Unit)? = null) {
//        if (videoPlayerParams.currentVideoItemBean.playerState==Player.STATE_READY){
        player.startPlay(videoPlayerParams.currentVideoUri, block)
        if (videoPlayerParams.currentVideoItemBean.currentPosition != null) {
            player.seekTo(videoPlayerParams.currentVideoItemBean.currentPosition)
        }
//        }else{
//            player.pause()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Enums.FileRequestCode.VIDEO.ordinal && resultCode == AppCompatActivity.RESULT_OK) {
            // 处理选择的视频文件
            val videoAddress = data?.data
            localFileUri = videoAddress.toString()
            val fileBean = FileBean(context, localFileUri)
            binding.chooseFilePathText.text = fileBean.fileName
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
            WebSocketServer(InetSocketAddress("::", port)) {
            override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
                websockets.add(conn)
                send()

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
            videoPlayerParams = VideoPlayerParams.getInstance().toClass(message)
            LogUtil.d(TAG, videoPlayerParams.toString())
            if (videoPlayerParams != null) {
                startPlay()
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

        fun send() {
            send(videoPlayerParams)
        }
    }

    companion object {
        private var myWebSocket: MyWebSocket? = null

        @JvmStatic
        fun newInstance() =
            VideoPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}