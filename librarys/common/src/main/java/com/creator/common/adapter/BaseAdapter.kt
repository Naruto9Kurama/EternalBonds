package com.creator.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.creator.common.utils.ReflectionUtil
import java.lang.reflect.ParameterizedType

abstract class BaseAdapter<T : ViewBinding>(var context: Context) :
    RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>() {
    val TAG=this.javaClass.simpleName


    inner class ViewHolder(val v: T) : RecyclerView.ViewHolder(v.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //利用反射，调用指定ViewBinding中的inflate方法填充视图
        var binding: T =
            ReflectionUtil.generateBinding(javaClass.genericSuperclass, context, parent)!!

        return ViewHolder(binding)
    }
}