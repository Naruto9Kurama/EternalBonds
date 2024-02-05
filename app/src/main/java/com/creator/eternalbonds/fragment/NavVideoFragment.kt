package com.creator.eternalbonds.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.creator.common.Constants
import com.creator.common.bean.VideoPlayerParams
import com.creator.common.enums.Enums
import com.creator.common.fragment.BaseFragment
import com.creator.common.utils.AdUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.ToastUtil
import com.creator.eternalbonds.R
import com.creator.eternalbonds.activity.VideoActivity
import com.creator.eternalbonds.databinding.DialogClientInputBinding
import com.creator.eternalbonds.databinding.FragmentNavVideoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.nativead.NativeAd
import com.huawei.hms.ads.nativead.NativeAdLoader
import com.huawei.hms.ads.nativead.NativeView


/**
 * A simple [Fragment] subclass.
 * Use the [NavVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavVideoFragment : BaseFragment<FragmentNavVideoBinding>() {
    override fun initView() {
        AdUtil.setBanner(binding.hwBannerViewBottom)
        AdUtil.setBanner(binding.hwBannerView, BannerAdSize.BANNER_SIZE_360_144)
        binding.hwBannerViewBottom!!.adListener = adListener
    }

    private val adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            // 广告加载成功时调用
            LogUtil.d(TAG,"广告打开时调用")
        }
        override fun onAdFailed(errorCode: Int) {
            // 广告加载失败时调用
            LogUtil.d(TAG,"广告加载失败时调用"+errorCode)
        }
        override fun onAdOpened() {
            // 广告打开时调用
            LogUtil.d(TAG,"广告打开时调用")
        }
        override fun onAdClicked() {
            // 广告点击时调用
            LogUtil.d(TAG,"广告点击时调用")
        }
        override fun onAdLeave() {
            // 广告离开应用时调用
            LogUtil.d(TAG,"广告离开应用时调用")
        }
        override fun onAdClosed() {
            // 广告关闭时调用
            LogUtil.d(TAG,"onAdClosed")
        }
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
                if (serverIp.isNotEmpty() && VideoPlayerParams.getInstance()
                        .setServerIp(serverIp)
                ) {
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