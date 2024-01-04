package com.creator.eternalbonds.activity

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.utils.IPUtil
import com.creator.eternalbonds.databinding.ActivityVideoBinding
import com.creator.exoplayer.fragment.VideoPageFragment
import com.creator.exoplayer.fragment.VideoPlayerFragment

class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 添加布局监听器
        binding.videoPage.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // 移除监听器，以免重复调用
                binding.videoPage.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


        val fragment = VideoPageFragment.newInstance()

        // 使用 FragmentManager 启动 Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.videoPage.id, fragment)
            .commit()

    }
}