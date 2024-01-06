package com.creator.eternalbonds.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.utils.IPUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.ToastUtil
import com.creator.eternalbonds.databinding.ActivityMainBinding
import java.net.URI
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var videoAddress: String
    private lateinit var uri: URI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        PermissionUtils.requestFilePermissions((Activity) context);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val future = CompletableFuture<Boolean>()
        IPUtil.getIpAddress(block = { ip, ips ->
            try {
                runOnUiThread {
                    if (ips.isNotEmpty()) {
                        VideoPlayerParams.getInstance().myIps = ips
                        binding.openServeBtn.isClickable = true
//                binding.openClientBtn.isClickable = true
                    } else {
                        binding.openServeBtn.text = "没有可用的ip地址，无法使用服务端"
                    }
                    addListener()
                }
            } catch (e: Exception) {

            } finally {
                future.complete(true)
            }
        })

        if (future.get()) {
            LogUtil.d(TAG, "加载完成")
        }

    }

    fun addListener() {
        //打开服务端
        binding.openServeBtn.setOnClickListener {
            VideoPlayerParams.getInstance().playerRole = Enums.PlayerRole.Server
            startActivity(Intent(this, VideoActivity::class.java))
        }
        //打开客户端
        binding.openClientBtn.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            val serverIp = binding.ipEdit.text.toString()
            if (serverIp.isNotEmpty() && VideoPlayerParams.getInstance().setServerIp(serverIp)) {
                VideoPlayerParams.getInstance().playerRole = Enums.PlayerRole.Client
                startActivity(intent)
            } else {
                ToastUtil.show(this, "请先输入有效的服务端IP")
            }
        }
    }


    /**
     * A native method that is implemented by the 'eternalbonds' native library,
     * which is packaged with this application.
     */
//    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'eternalbonds' library on application startup.
        init {
            System.loadLibrary("eternalbonds")
        }
    }
}