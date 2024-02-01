package com.creator.common.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.creator.common.utils.ReflectionUtil
import com.creator.common.utils.ThemeModeUtil
import java.lang.reflect.ParameterizedType

 open class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    lateinit var binding: T

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用沉浸式模式
        entryImmersiveMode()
        //利用反射，调用指定ViewBinding中的inflate方法填充视图

        kotlin.runCatching {
            binding = ReflectionUtil.generateBinding(javaClass.genericSuperclass, layoutInflater)!!
            setContentView(binding.root)
        }
        init()
        addListener()
    }

    /**
     * 初始化组件
     */
    open fun init(){}

    /**
     * 添加监听事件
     */
    open fun addListener(){}

    /**
     * 沉浸模式
     * @param isImmersiveMode 是否进入沉浸模式
     * @param isHideNavigation 是否隐藏导航栏
     * @param isFullscreen 是否全屏
     * @param isLightStatusBar 状态栏文字是否为灰色
     */
    fun setSystemUi(
        isImmersiveMode: Boolean,
        isHideNavigation: Boolean = true,
        isFullscreen: Boolean = true,
        isLightStatusBar: Boolean = !ThemeModeUtil.isDarkMode(this),
    ) {
        var i =
            if (isImmersiveMode) View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else View.SYSTEM_UI_FLAG_VISIBLE
        if (isHideNavigation) {
            i = i or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        if (isFullscreen) {
            i = i or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        if (isLightStatusBar) {
            i = i or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = i
    }

    /**
     * 进入沉浸模式
     */
    fun entryImmersiveMode() {
        setSystemUi(true, false, false)
    }

    /**
     * 进入全屏模式
     */
    fun entryFullscreen() {
        setSystemUi(true, true, true)
    }
}