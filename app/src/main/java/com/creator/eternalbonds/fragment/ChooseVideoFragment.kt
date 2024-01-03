package com.creator.eternalbonds.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.creator.common.bean.VideoItemBean
import com.creator.common.bean.VideoPlayerParams
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

    private lateinit var localFileUri: String
    private val videoPlayerParams: VideoPlayerParams = VideoPlayerParams.getInstance()
    private val videoItemBeanList: MutableList<VideoItemBean> = VideoPlayerParams.getInstance().videoItemBeanList

    private val videoItemBean:VideoItemBean= VideoItemBean()
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
            videoItemBean.playbackSource= when (checkedId) {
                localFilesRadioButton.id -> {
                    Enums.PlaybackSource.LOCAL_FILES
                }

                httpRadioButton.id -> {
                    Enums.PlaybackSource.HTTP
                }

                screenCastingRadioButton.id -> {
                    Enums.PlaybackSource.SCREEN_CASTING
                }

                else -> {
                    Enums.PlaybackSource.LOCAL_FILES
                }
            }
        }
        //打开服务端播放器按钮
        binding.startPlayer.setOnClickListener {
            //创建VideoActivity意图对象
            val intent = Intent(activity, VideoActivity::class.java)
            videoItemBean.playerRole = Enums.PlayerRole.Server
            when (videoItemBean.playbackSource) {
                Enums.PlaybackSource.LOCAL_FILES -> {
                    videoItemBean.uri=localFileUri
                }

                Enums.PlaybackSource.HTTP -> {
                    videoItemBean.uri= binding.httpEditText.text.toString()
                }

                Enums.PlaybackSource.SCREEN_CASTING -> {

                }
            }
            videoItemBeanList.add(videoItemBean)
            videoPlayerParams.videoItemBeanList=videoItemBeanList
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
            videoItemBean.ip=binding.ipEditText.text.toString()
            startActivity(intent)
        }


        /*//http编辑框文本监听事件
        binding.httpEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                videoItemBean.uri = s.toString();
            }
        })*/
    }


    /**
     * 选择文件后回调方法
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Enums.FileRequestCode.VIDEO.ordinal && resultCode == AppCompatActivity.RESULT_OK) {
            // 处理选择的视频文件
            val videoAddress = data?.data
            localFileUri =videoAddress.toString()
            val filePathFromUri =
                FileUtil.getRealPathFromUri(requireContext(), Uri.parse(videoAddress.toString()))
            binding.chooseFilePathText.text =filePathFromUri
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