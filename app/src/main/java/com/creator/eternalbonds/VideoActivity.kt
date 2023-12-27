package com.creator.eternalbonds

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.enums.Enums
import com.creator.eternalbonds.databinding.ActivityVideoBinding
import com.creator.exoplayer.fragment.VideoPlayerFragment
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

                // 移除监听器，以免重复调用
                binding.playerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


        // 启动 Fragment，并传递枚举值
        // 从 Intent 中获取枚举值的字符串表示
        // 从 Intent 中获取枚举值的字符串表示
        val enumString = intent.getStringExtra("test")?:""

        val enumValue = Enums.VideoRole.valueOf(enumString)
        val fragment = VideoPlayerFragment.newInstance(enumValue)

        // 使用 FragmentManager 启动 Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.playerView.id, fragment)
            .commit()

    }
}