package com.creator.common.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.creator.common.activity.BaseActivity
import com.creator.common.utils.ReflectionUtil

open class BaseFragment<T : ViewBinding> : Fragment() {
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
        initView(inflater, container, savedInstanceState)
        addListener(inflater, container, savedInstanceState)
        return binding.root
    }

    /**
     * 初始化组件
     */
    open fun initView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?){}

    /**
     * 添加监听事件
     */
    open fun addListener(inflater: LayoutInflater, container: ViewGroup?,
                             savedInstanceState: Bundle?){}
    /**
     * 进入沉浸模式
     */
    fun entryImmersiveMode() {
        (activity as BaseActivity<*>).entryImmersiveMode()
    }

    fun entryFullscreen() {
        (activity as BaseActivity<*>).entryFullscreen()
    }
}