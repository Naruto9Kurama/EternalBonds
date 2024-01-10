package com.creator.eternalbonds.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import com.creator.common.activity.BaseActivity
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.utils.IPUtil
import com.creator.common.utils.ToastUtil
import com.creator.eternalbonds.R
import com.creator.eternalbonds.databinding.ActivityMainBinding
import com.creator.eternalbonds.fragment.VideoFragment
import com.google.android.material.navigation.NavigationBarView


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun init() {
        replaceFragment(VideoFragment())
        //获取ip地址
        IPUtil.getIpAddress{allIps,pubIps,priIps->
            if (allIps.isNotEmpty()) {
                VideoPlayerParams.getInstance().myIps = allIps
            }
            if (pubIps.isNotEmpty()) {
                VideoPlayerParams.getInstance().myPublicIps = pubIps
            }
            if (priIps.isNotEmpty()) {
                VideoPlayerParams.getInstance().myPrivateIps = priIps
            }
        }

    }

    override fun addListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when(it.itemId){
                R.id.nav_video->{
                    replaceFragment(VideoFragment())
                }
            }
            return@OnItemSelectedListener true
        })
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.homeFrameLayout.id, fragment)
            .commit()
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