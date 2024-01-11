package com.creator.eternalbonds.activity

import android.view.ViewTreeObserver
import com.creator.common.activity.BaseActivity
import com.creator.eternalbonds.databinding.ActivityVideoBinding
import com.creator.eternalbonds.fragment.VideoPageFragment

class VideoActivity : BaseActivity<ActivityVideoBinding>()  {

    override fun init() {

        val fragment = VideoPageFragment.newInstance()

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