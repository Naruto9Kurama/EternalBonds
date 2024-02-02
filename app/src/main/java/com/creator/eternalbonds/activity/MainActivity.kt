package com.creator.eternalbonds.activity

import androidx.fragment.app.Fragment
import com.creator.common.Constants
import com.creator.common.activity.BaseActivity
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.utils.IPUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.NetworkUtils
import com.creator.eternalbonds.R
import com.creator.eternalbonds.databinding.ActivityMainBinding
import com.creator.eternalbonds.fragment.NavVideoFragment
import com.creator.eternalbonds.fragment.RemoteControllerFragment
import com.google.android.material.navigation.NavigationBarView


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun init() {
        Constants.Data.Ip
        replaceFragment(NavVideoFragment())
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
        LogUtil.d(TAG,NetworkUtils.isNetworkAvailable(this).toString())

    }

    override fun addListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when(it.itemId){
                R.id.nav_video->{
                    replaceFragment(NavVideoFragment())
                }
                R.id.nav_remote_controller->{
                    replaceFragment(RemoteControllerFragment())
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