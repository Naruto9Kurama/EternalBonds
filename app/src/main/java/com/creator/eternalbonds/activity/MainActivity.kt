package com.creator.eternalbonds.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.fuwuduan.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }
        binding.kehuduan.setOnClickListener {

            val intent = Intent(this, VideoActivity::class.java)
//            intent.putExtra("ip",binding.ipEdit.text.toString())
            VideoPlayerParams.getInstance().serverIp = binding.ipEdit.text.toString()
            startActivity(intent)
        }

        IPUtil.getIpAddress { ip ->
            if (IPUtil.isIpv6(ip)) {
                runOnUiThread {
                    VideoPlayerParams.getInstance().myIp = ip.toString()
                    binding.ipText.text = ip.toString()
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