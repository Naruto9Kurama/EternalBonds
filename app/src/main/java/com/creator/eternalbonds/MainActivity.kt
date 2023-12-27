package com.creator.eternalbonds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.enums.Enums
import com.creator.eternalbonds.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.faqi.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("test",Enums.VideoRole.Server.name)
            startActivity(intent)
        }

        binding.rec.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("test",Enums.VideoRole.Client.name)
            val text = binding.ipEdit.text
            intent.putExtra("ip", text.toString())
            startActivity(intent)
        }

        //27.149.25.162
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