package com.creator.eternalbonds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creator.eternalbonds.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.faqi.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }

        binding.rec.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }

        /*val webSocketServer = WebSocketServer()
        webSocketServer.start()*/

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