package com.creator.eternalbonds

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.enums.Enums
import com.creator.common.utils.LogUtil
import com.creator.eternalbonds.databinding.ActivityMainBinding
import com.creator.nanohttpd.server.VideoNanoHttpDServer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {

    private val TAG="MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var videoAddress:String
    private lateinit var uri: URI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.faqi.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("test",Enums.VideoRole.Server.name)
            intent.putExtra("filePath",uri.toString())
            startActivity(intent)
//            VideoNanoHttpDServer(videoUri = uri, context = this).start()
//            VideoNanoHttpDServer(uri = "http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4").start()
        }

        binding.rec.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("test",Enums.VideoRole.Client.name)
            val text = binding.ipEdit.text
            intent.putExtra("ip", text.toString())
            startActivity(intent)
        }

        binding.chooseFile.setOnClickListener {
            // 启动文件选择器
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, Enums.FileRequestCode.VIDEO.ordinal)
        }


        //27.149.25.162
        /*val webSocketServer = WebSocketServer()
        webSocketServer.start()*/

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Enums.FileRequestCode.VIDEO.ordinal && resultCode == RESULT_OK) {
            // 处理选择的视频文件
            val videoAddress = data?.data
             uri = URI.create(videoAddress.toString())
            LogUtil.d(TAG, uri.toString())
            // 这里可以使用 selectedVideoUri 操作选择的视频文件
        }
    }







    /**
     * A native method that is implemented by the 'eternalbonds' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'eternalbonds' library on application startup.
        init {
            System.loadLibrary("eternalbonds")
        }
    }
}