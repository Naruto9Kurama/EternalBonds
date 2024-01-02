package com.creator.eternalbonds.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.enums.Enums
import com.creator.common.utils.FileUtil
import com.creator.common.utils.LogUtil
import com.creator.eternalbonds.activity.VideoActivity
import com.creator.eternalbonds.databinding.FragmentChooseVideoBinding
import com.creator.exoplayer.fragment.VideoPlayerFragment
import java.net.URI

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 选择视频Fragment
 */
class ChooseVideoFragment : Fragment() {
    private val TAG = "ChooseVideoFragment"
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentChooseVideoBinding
    private lateinit var localFilesRadioButton: RadioButton
    private lateinit var httpRadioButton: RadioButton
    private lateinit var screenCastingRadioButton: RadioButton
    private var playbackSource: Enums.PlaybackSource = Enums.PlaybackSource.LOCAL_FILES

    private lateinit var localFileUri: URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseVideoBinding.inflate(inflater)
        init()
        return binding.root
    }

    /**
     * 初始化对象
     */
    private fun init() {
        //本地文件RadioButton
        localFilesRadioButton = binding.localFilesRadioButton
        //Http RadioButton
        httpRadioButton = binding.httpRadioButton
        //投屏RadioButton
        screenCastingRadioButton = binding.screenCastingRadioButton
        addListener()
    }

    /**
     * 添加监听
     */
    private fun addListener() {
        //选择播放源按钮组
        binding.playbackRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            //判断点击了哪个radio按钮
            when (checkedId) {
                localFilesRadioButton.id -> {
                    playbackSource = Enums.PlaybackSource.LOCAL_FILES
                }

                httpRadioButton.id -> {
                    playbackSource = Enums.PlaybackSource.HTTP
                }

                screenCastingRadioButton.id -> {
                    playbackSource = Enums.PlaybackSource.SCREEN_CASTING
                }
            }
        }
        //打开服务端播放器按钮
        binding.startPlayer.setOnClickListener {
            //创建VideoActivity意图对象
            val intent = Intent(activity, VideoActivity::class.java)
            //添加传输数据
            //播放器角色
            intent.putExtra(
                VideoPlayerFragment.Companion.ParamKey.VIDEO_ROLE.name,
                Enums.PlayerRole.Server.name
            )
            //判断播放源
            when (playbackSource) {
                //本地文件
                Enums.PlaybackSource.LOCAL_FILES -> {
                    intent.putExtra(
                        VideoPlayerFragment.Companion.ParamKey.VIDEO_URI.name,
                        localFileUri.toString()
                    )
                }

                //HTTP文件
                Enums.PlaybackSource.HTTP -> {
                    LogUtil.d(TAG, binding.httpEditText.text.toString(), null)
                    intent.putExtra(
                        VideoPlayerFragment.Companion.ParamKey.VIDEO_URI.name,
                        binding.httpEditText.text.toString()
                    )
                }

                //投屏
                Enums.PlaybackSource.SCREEN_CASTING -> {
                }
            }
            startActivity(intent)
        }
        //选择文件按钮
        binding.chooseFileBtn.setOnClickListener {
            // 启动文件选择器
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, Enums.FileRequestCode.VIDEO.ordinal)
        }
        //打开客户端播放器按钮
        binding.startClientPlayer.setOnClickListener {
            val intent = Intent(activity, VideoActivity::class.java)
            intent.putExtra(
                VideoPlayerFragment.Companion.ParamKey.VIDEO_ROLE.name,
                Enums.PlayerRole.Client.name
            )
            val ipText = binding.ipEditText.text
            intent.putExtra(VideoPlayerFragment.Companion.ParamKey.SERVER_IP.name, ipText.toString())
            startActivity(intent)
        }
    }


    /**
     * 选择文件后回调方法
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Enums.FileRequestCode.VIDEO.ordinal && resultCode == AppCompatActivity.RESULT_OK) {
            // 处理选择的视频文件
            val videoAddress = data?.data
            localFileUri = URI.create(videoAddress.toString())
            LogUtil.d(TAG, localFileUri.toString())
            val filePathFromUri =
                FileUtil.getRealPathFromUri(requireContext(), Uri.parse(videoAddress.toString()))
            binding.chooseFilePathText.text = filePathFromUri
            // 这里可以使用 selectedVideoUri 操作选择的视频文件
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChooseVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}