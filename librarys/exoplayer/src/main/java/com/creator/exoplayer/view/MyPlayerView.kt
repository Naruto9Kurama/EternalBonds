package com.creator.exoplayer.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.creator.exoplayer.databinding.PlayerBinding
import com.creator.exoplayer.player.ExoPlayerSingleton
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MyPlayerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    lateinit var binding: PlayerBinding
    lateinit var exoPlayer: ExoPlayer

    init {
        // 使用视图绑定生成的类
        binding = PlayerBinding.inflate(LayoutInflater.from(context), this, true)
        exoPlayer = ExoPlayerSingleton.getExoPlayer(context)
        binding.playerView.player = exoPlayer
    }
}