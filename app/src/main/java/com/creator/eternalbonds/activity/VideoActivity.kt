package com.creator.eternalbonds.activity

import android.view.ViewTreeObserver
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.creator.common.Constants
import com.creator.common.activity.BaseActivity
import com.creator.common.enums.Enums
import com.creator.eternalbonds.databinding.ActivityVideoBinding
import com.creator.eternalbonds.fragment.VideoPageFragment

class VideoActivity : BaseActivity<ActivityVideoBinding>()  {

    @OptIn(UnstableApi::class) override fun init() {
        val stringExtra = intent.getStringExtra(Constants.Key.Video.VideoServer)

        val fragment = VideoPageFragment.newInstance(Enums.PlayerRole.valueOf(stringExtra!!))

        // 使用 FragmentManager 启动 Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.videoPage.id, fragment)
            .commit()
    }




    override fun addListener() {
        // 添加布局监听器
        binding.videoPage.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // 移除监听器，以免重复调用
                binding.videoPage.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}