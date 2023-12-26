package com.creator.eternalbonds

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.creator.eternalbonds.databinding.ActivityVideoBinding
import com.creator.exoplayer.player.ExoPlayerSingleton

class VideoActivity: AppCompatActivity()  {
    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 添加布局监听器
        binding.playerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 在布局加载完成后执行你的操作
                // 例如，可以在这里动态添加 Fragment
                // 或者执行与 FragmentContainerView 相关的其他操作
                ExoPlayerSingleton.setSource("http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4",this@VideoActivity)
                ExoPlayerSingleton.play()
                // 移除监听器，以免重复调用
                binding.playerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


    }
}