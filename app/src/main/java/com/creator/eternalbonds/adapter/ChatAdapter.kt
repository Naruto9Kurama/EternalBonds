package com.creator.eternalbonds.adapter

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.creator.common.adapter.BaseAdapter
import com.creator.common.bean.ChatItemBean
import com.creator.eternalbonds.databinding.ItemChatBinding

class ChatAdapter(context: Context) : BaseAdapter<ItemChatBinding>(context) {

    var chatItemBeans: ArrayList<ChatItemBean> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var chatItemBean = chatItemBeans[position]
        holder.v.text.text = chatItemBean.message
    }

    override fun getItemCount(): Int {
        return chatItemBeans.size;
    }
}