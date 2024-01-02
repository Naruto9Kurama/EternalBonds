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

        // 启动 Fragment，并传递枚举值
        // 从 Intent 中获取枚举值的字符串表示
        var map = HashMap<String, Any?>()
        map[VideoPlayerFragment.Companion.ParamKey.VIDEO_ROLE.name] =
            intent.getStringExtra(VideoPlayerFragment.Companion.ParamKey.VIDEO_ROLE.name)
        map[VideoPlayerFragment.Companion.ParamKey.VIDEO_URI.name] =
            intent.getStringExtra(VideoPlayerFragment.Companion.ParamKey.VIDEO_URI.name)
        map[VideoPlayerFragment.Companion.ParamKey.SERVER_IP.name] =
            intent.getStringExtra(VideoPlayerFragment.Companion.ParamKey.SERVER_IP.name)
        val fragment = VideoPlayerFragment.newInstance(map)

        // 使用 FragmentManager 启动 Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.playerView.id, fragment)
            .commit()

        IPUtil.getIpv4Address { ip ->
            if (IPUtil.isPublicIPv6(ip)) {
                runOnUiThread {
                    binding.ipText.text = ip
                }
            } else {

            }
        }
    }
}