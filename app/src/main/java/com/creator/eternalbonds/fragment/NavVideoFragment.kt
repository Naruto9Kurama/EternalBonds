package com.creator.eternalbonds.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.fragment.BaseFragment
import com.creator.common.utils.ToastUtil
import com.creator.eternalbonds.activity.VideoActivity
import com.creator.eternalbonds.databinding.DialogClientInputBinding
import com.creator.eternalbonds.databinding.FragmentNavVideoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * A simple [Fragment] subclass.
 * Use the [NavVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavVideoFragment : BaseFragment<FragmentNavVideoBinding>() {
    override fun initView() {

    }

    override fun addListener() {
        //打开服务端
        binding.openServeBtn.setOnClickListener {
            VideoPlayerParams.getInstance().playerRole = Enums.PlayerRole.Server
            context?.startActivity(Intent(context, VideoActivity::class.java))
        }
        //打开客户端
        binding.openClientBtn.setOnClickListener {
            //打开输入ip弹窗
            val ipInputLayout = DialogClientInputBinding.inflate(layoutInflater)
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(" 请输入服务端ip")
                .setView(ipInputLayout.root)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .create()
            dialog.show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val intent = Intent(context, VideoActivity::class.java)
                    val serverIp = ipInputLayout.ipInput.text.toString()
                    if (serverIp.isNotEmpty() && VideoPlayerParams.getInstance().setServerIp(serverIp) ) {
                        VideoPlayerParams.getInstance().playerRole = Enums.PlayerRole.Client
                        startActivity(intent)
                    } else {
                        ToastUtil.show(context, "请先输入有效的服务端IP")
                    }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavVideoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}