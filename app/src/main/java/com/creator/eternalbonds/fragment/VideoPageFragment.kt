package com.creator.eternalbonds.fragment

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.creator.common.Constants
import com.creator.common.MainThreadExecutor
import com.creator.common.bean.FileBean
import com.creator.common.bean.WebSocketMessageBean
import com.creator.common.bean.player.VideoPlayerDataBean
import com.creator.common.enums.Enums
import com.creator.common.fragment.BaseFragment
import com.creator.common.utils.AppPermissionUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.ScreenUtil
import com.creator.common.utils.ToastUtil
import com.creator.eternalbonds.adapter.IPAdapter
import com.creator.eternalbonds.databinding.FragmentVideoPageBinding
import com.creator.eternalbonds.listener.PlayerListener
import com.creator.nanohttpd.server.VideoNanoHttpDServer
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.net.URI

@UnstableApi
class VideoPageFragment : BaseFragment<FragmentVideoPageBinding>(),VideoPlayerFragment.PlayerLoadProgress {

    lateinit var playerFragment: VideoPlayerFragment
    var videoNanoHttpDServer: VideoNanoHttpDServer? = null


    private var isSeekTo = false


    /*---------new-----------------*/
    private lateinit var videoServer: Enums.PlayerRole//播放器角色
    private val videoPlayerDataBean: VideoPlayerDataBean = VideoPlayerDataBean()//播放器数据
    private var isServer: Boolean = false//播放器是否是服务端
    private var fileBean: FileBean? = null//选择的文件
    private lateinit var myWebSocket: MyWebSocket
    private var localFileUri: String? = null

    //监听事件
    val listener = PlayerListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取数据
        arguments?.let {
            //获取播放器角色信息
            videoServer = Enums.PlayerRole.valueOf(it.getString(Constants.Key.Video.VideoServer)!!)
            isServer = videoServer == Enums.PlayerRole.Server
        }

    }

    /**
     * 初始化对象
     */
    override fun initView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        playerFragment = VideoPlayerFragment(this)
        // 使用 FragmentManager 启动 Fragment
        childFragmentManager.beginTransaction().replace(binding.playerView.id, playerFragment)
            .commit()
        myWebSocket = MyWebSocket()

        if (!isServer) {
            //客户端
            binding.btnOpenDrawer.visibility = View.GONE
        } else {
            val ipAdapter = IPAdapter(requireContext(), binding.ipTitleText)
            binding.ipRecycler.adapter = ipAdapter
            ipAdapter.pubIps = Constants.Data.Ip.pubIps
            ipAdapter.priIps = Constants.Data.Ip.priIps
            binding.ipRecycler.layoutManager = LinearLayoutManager(context)
        }

        binding.screenCastingRadioButton.visibility = View.GONE
    }

    override fun addListener(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        //播放按钮
        binding.startPlayer.setOnClickListener {
            when (binding.playbackRadioGroup.checkedRadioButtonId) {
                //本地文件
                binding.localFilesRadioButton.id -> {
                    if (localFileUri != null) {
                        //创建视频列表
                        val videoItemBean = com.creator.common.bean.player.VideoItemBean()
                        videoItemBean.playbackSource = Enums.PlaybackSource.LOCAL_FILES
                        videoItemBean.setLocalUri(localFileUri)
                        videoItemBean.ip = Constants.Data.Ip.myIp
                        videoPlayerDataBean.videoItemBeanList.add(videoItemBean)
                        startNano()
                    } else {
                        ToastUtil.show(context, "请先选择视频文件")
                        return@setOnClickListener
                    }
                }
                //http
                binding.httpRadioButton.id -> {
                    val httpUri = binding.httpEditText.text.toString()
                    if (httpUri.isNotEmpty()) {
//                        videoItemBean.uri = httpUri
//                        videoItemBean.playbackSource = Enums.PlaybackSource.HTTP
                        closeNano()
                    } else {
                        ToastUtil.show(context, "请先输入视频http地址")
                        return@setOnClickListener
                    }

                }
                //投屏
                binding.screenCastingRadioButton.id -> {
                }

                else -> {
                }
            }

            startPlay()

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



    fun seekTo(l: Long) {
        isSeekTo = true
        playerFragment.exoPlayer.seekTo(l)
    }

    /**
     * 开启Nano
     */
    fun startNano() {
        if (videoNanoHttpDServer == null) {
            videoNanoHttpDServer = VideoNanoHttpDServer(
                uri = videoPlayerDataBean.videoItemBeanList.get(videoPlayerDataBean.currentIndex).uri,
                context = context
            )
            videoNanoHttpDServer?.start()
            ToastUtil.show(context, "NanoHttpD已启动")
        } else {
            videoNanoHttpDServer?.setVideoUri(
                videoPlayerDataBean.videoItemBeanList.get(
                    videoPlayerDataBean.currentIndex
                ).uri
            )
        }
    }

    fun closeNano() {
        if (videoNanoHttpDServer != null) {
            videoNanoHttpDServer?.stop()
            videoNanoHttpDServer = null
            ToastUtil.show(context, "已关闭NanoHttpD")
        }

    }

    fun play() {
        MainThreadExecutor.runOnUiThread {
            playerFragment.exoPlayer.play()
        }
    }

    /**
     * 开始播放
     */
    fun startPlay(block: (() -> Unit)? = null) {
        playerFragment.setMediaSource(
            videoPlayerDataBean.videoItemBeanList[videoPlayerDataBean.currentIndex],
            true
        )
    }

    /**
     * 播放器视图加载完成回调
     */
    override fun onLoadingFinish() {
        //添加播放器监听
        playerFragment.exoPlayer.addListener(listener)
    }
    /**
     * 选择文件回调方法
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Enums.FileRequestCode.VIDEO.ordinal && resultCode == AppCompatActivity.RESULT_OK) {
            // 处理选择的视频文件
            val videoAddress = data?.data
            localFileUri = videoAddress.toString()
            fileBean = FileBean(context, localFileUri)
            binding.chooseFilePathText.text = fileBean?.fileName
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        myWebSocket.close()
        closeNano()
    }

    fun sendMessage( message:String){
//        myWebSocket.
    }

    /**
     * websocketk类
     */
    inner class MyWebSocket {

        private var websockets = HashSet<WebSocket>()
        private var isReadyMap = HashMap<WebSocket, Boolean>()
        private lateinit var exoPlayerWebSocketServer: ExoPlayerWebSocketServer
        private lateinit var exoPlayerWebSocketClient: ExoPlayerWebSocketClient

        init {
            //根据播放器角色创建对应的websocket类
            if (isServer) {
                exoPlayerWebSocketServer = ExoPlayerWebSocketServer()
                exoPlayerWebSocketServer.start()
            } else {
                exoPlayerWebSocketClient = ExoPlayerWebSocketClient()
                exoPlayerWebSocketClient.connect()
            }
        }

        inner class ExoPlayerWebSocketClient constructor(uri: String = videoPlayerDataBean.serverIp) :
            org.java_websocket.client.WebSocketClient(URI(uri)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                websockets.add(connection)
                ToastUtil.show(context, "连接成功")
            }

            override fun onMessage(message: String?) {
                LogUtil.d(TAG, message.toString())
                processMessages(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                LogUtil.e(TAG, "onClose$reason")
                websockets.clear()
                if (remote) {
                    ToastUtil.show(context, "对方已断开连接")

                    activity?.finish()
                } else {
                    ToastUtil.show(context, "重新连接")
                    connect()
                }
            }

            override fun onError(ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }
        }


        inner class ExoPlayerWebSocketServer constructor(port: Int = Constants.WebSocket.PORT) :
            WebSocketServer(InetSocketAddress("::", port)) {
            override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
                websockets.add(conn)
                isReadyMap[conn] = false;
//                send()
            }

            override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
                if (remote) {
                    ToastUtil.show(context, "${conn.toString()}已断开连接")
                    websockets.remove(conn)
                } else {
                    ToastUtil.show(context, "${conn.toString()}已断开连接")

                    activity?.finish()
                }
            }

            override fun onMessage(conn: WebSocket?, message: String?) {
                LogUtil.d(TAG, "onMessage:::$message")
                processMessages(message, conn)
            }

            override fun onError(conn: WebSocket?, ex: java.lang.Exception?) {
                LogUtil.e(TAG, "onError:::" + ex?.message, ex)
            }

            override fun onStart() {
                LogUtil.d(TAG, "onStart")
                ToastUtil.show(requireContext(), "WebSocket服务启动成功")
            }

            override fun start() {
                try {
                    super.start()
                } catch (e: Exception) {
                    ToastUtil.show(context, "websocket服务端口被占用,无法启动成功")
                    activity?.finish()
                }
            }
        }

        fun close() {
            kotlin.runCatching {
                //根据播放器角色创建对应的websocket类
                if (isServer) {
                    exoPlayerWebSocketServer.stop()

                } else {
                    exoPlayerWebSocketClient.close()

                }
//                myWebSocket = null
                ToastUtil.show(context, "已关闭websocket")
            }
        }

        fun processMessages(message: String?, conn: WebSocket? = null) {
            val webSocketMessageBean = WebSocketMessageBean.toClass(message)
            when (webSocketMessageBean.messageType) {
                Enums.MessageType.IS_READY -> {
                    isReadyMap[conn!!] = true
                    var isReady = 0
                    isReadyMap.forEach {
                        if (it.value) isReady++
                    }
                    if (isReady == isReadyMap.size) {
//                        send(Enums.MessageType.START_PLAY, "")
                        play()
                    }
                }

                Enums.MessageType.Video -> {
                    updateVideo(webSocketMessageBean.message)
                }

                Enums.MessageType.START_PLAY -> {
                    play()
                }
            }
        }

        /**
         * 通过websocket接收到的消息更新视频数据
         */
        fun updateVideo(message: String?) {
//            videoPlayerParams = VideoPlayerParams.getInstance().toClass(message)
//            LogUtil.d(TAG, videoPlayerParams.toString())
//            if (videoPlayerParams != null) {
//                startPlay()
//
//            }
        }

        /*fun send(videoPlayerParams: VideoPlayerParams) {
            send(Enums.MessageType.Video, videoPlayerParams.toString())
        }

        fun send(messageType: Enums.MessageType, message: String) {
            send(WebSocketMessageBean(messageType, message))
        }

        fun send(webSocketMessageBean: WebSocketMessageBean) {
            websockets.forEach { websocket ->
                try {
                    websocket.send(webSocketMessageBean.toString())
                } catch (e: Exception) {
                    LogUtil.e(TAG, "send失败：${webSocketMessageBean.toString()}\n" + e.message, e)
                }
            }
        }

        fun send() {
            send(videoPlayerParams)
        }*/
    }


    /**
     * 配置更改回调
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtil.d(TAG, "onOrientationChanged:::$newConfig")
        setPlayerFull(newConfig)//设置播放器全屏
    }
    /**
     * 判断是否需要设置播放器全屏
     */
    fun setPlayerFull(newConfig: Configuration) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullScreen(true)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setFullScreen(false)
        }
    }


    /**
     * 设置播放器是否全屏
     */
    private fun setFullScreen(fullScreen: Boolean) {
        val layoutParams = binding.playerView.layoutParams
        if (fullScreen) {
            layoutParams.height = LayoutParams.MATCH_PARENT

            // 隐藏状态栏和导航栏，并启用沉浸式模式
            entryFullscreen()
        } else {
            layoutParams.height = ScreenUtil.dip2px(context, 200f)
            entryImmersiveMode()
        }
        binding.playerView.layoutParams = layoutParams
    }

    companion object {

        @JvmStatic
        fun newInstance(videoServer: Enums.PlayerRole) =
            VideoPageFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.Key.Video.VideoServer, videoServer.name)
                }
            }
    }

}