package com.creator.eternalbonds.activity

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.utils.IPUtil
import com.creator.eternalbonds.databinding.ActivityMainBinding
import java.net.URI


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var videoAddress: String
    private lateinit var uri: URI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openServeBtn.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }
        binding.openClientBtn.setOnClickListener {

            val intent = Intent(this, VideoActivity::class.java)
//            intent.putExtra("ip",binding.ipEdit.text.toString())
            VideoPlayerParams.getInstance().serverIp = binding.ipEdit.text.toString()
            startActivity(intent)
        }

        IPUtil.getIpAddress { ip ->
            if (IPUtil.isIpv6(ip)) {
                runOnUiThread {
                    VideoPlayerParams.getInstance().myIp = ip.toString()
                    binding.ipText.text = "你的ip地址为："+ip.toString()
                }
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