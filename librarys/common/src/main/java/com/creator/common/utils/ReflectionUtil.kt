package com.creator.common.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 反射工具
 */
object ReflectionUtil {

    inline fun <T : ViewBinding> generateBinding(type: Type,context: Context,parent:ViewGroup?=null,attachToRoot:Boolean=false): T? {
        return generateBinding<T>(type,LayoutInflater.from(context),parent, attachToRoot)
    }

    //利用反射，调用指定ViewBinding中的inflate方法填充视图
    inline fun <T : ViewBinding> generateBinding(type: Type,layoutInflater: LayoutInflater,parent:ViewGroup?=null,attachToRoot:Boolean=false): T? {
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<T>

            val method = clazz.getMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
            return method.invoke(null,layoutInflater,parent,attachToRoot) as T
        }
        return null
    }

}