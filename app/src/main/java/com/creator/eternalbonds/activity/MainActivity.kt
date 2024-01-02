package com.creator.eternalbonds.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.enums.Enums
import com.creator.common.utils.LogUtil
import com.creator.eternalbonds.databinding.ActivityMainBinding
import com.creator.eternalbonds.fragment.ChooseVideoFragment
import com.creator.exoplayer.fragment.VideoPlayerFragment
import java.net.URI


class MainActivity : AppCompatActivity() {

    private val TAG="MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var videoAddress:String
    private lateinit var uri: URI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = ChooseVideoFragment.newInstance("","")

        // 使用 FragmentManager 启动 Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.fragment.id, fragment)
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