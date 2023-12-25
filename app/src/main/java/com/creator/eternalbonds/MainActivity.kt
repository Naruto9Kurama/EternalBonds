package com.creator.eternalbonds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creator.eternalbonds.databinding.ActivityMainBinding
import com.creator.exoplayer.player.ExoPlayerSingleton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ExoPlayerSingleton.setSource("http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4",this)
        ExoPlayerSingleton.play()

    }

    /**
     * A native method that is implemented by the 'eternalbonds' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'eternalbonds' library on application startup.
        init {
            System.loadLibrary("eternalbonds")
        }
    }
}