package com.creator.eternalbonds.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.creator.common.enums.Enums
import com.creator.common.fragment.BaseFragment
import com.creator.eternalbonds.R
import com.creator.eternalbonds.databinding.FragmentNavVideoBinding
import com.creator.eternalbonds.databinding.FragmentRemoteControllerBinding
import com.creator.remote_controller.activity.RemoteControllerActivity

/**
 * A simple [Fragment] subclass.
 * Use the [RemoteControllerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemoteControllerFragment : BaseFragment<FragmentRemoteControllerBinding>() {


    override fun initView() {

    }

    override fun addListener() {
        binding.serverBtn.setOnClickListener {
            val intent = Intent(context, RemoteControllerActivity::class.java)
            intent.putExtra("remoteRole", Enums.RemoteRole.Server)
            startActivity(intent)
        }


        binding.clientBtn.setOnClickListener {
            val intent = Intent(context, RemoteControllerActivity::class.java)
            intent.putExtra("remoteRole", Enums.RemoteRole.Client.name)
            intent.putExtra("uri", "")
            startActivity(intent)
        }

    }



}