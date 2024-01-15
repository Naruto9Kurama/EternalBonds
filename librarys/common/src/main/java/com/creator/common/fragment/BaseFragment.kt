package com.creator.common.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.creator.common.utils.ReflectionUtil

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    lateinit var binding: T
    val TAG=this.javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReflectionUtil.generateBinding<T>(javaClass.genericSuperclass, layoutInflater)!!
        initView()
        addListener()
        return binding.root
    }

    /**
     * 初始化组件
     */
    abstract fun initView();

    /**
     * 添加监听事件
     */
    abstract fun addListener();

}