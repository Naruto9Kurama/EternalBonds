package com.creator.eternalbonds.activity

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.utils.IPUtil
import com.creator.eternalbonds.databinding.ActivityVideoBinding
import com.creator.exoplayer.fragment.VideoPlayerFragment

class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 添加布局监听器
        binding.playerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // 移除监听器，以免重复调用
                binding.playerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        val fragment = VideoPlayerFragment.newInstance()

        // 使用 FragmentManager 启动 Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.playerView.id, fragment)
            .commit()

        IPUtil.getIpAddress { ip ->
            if (IPUtil.isPublicIPv6(ip)) {
                runOnUiThread {
                    binding.ipText.text = ip
                }
            } else {

            }
        }
    }
}