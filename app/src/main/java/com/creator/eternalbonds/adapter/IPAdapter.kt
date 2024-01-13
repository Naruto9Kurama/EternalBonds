package com.creator.eternalbonds.adapter

import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.creator.common.adapter.BaseAdapter
import com.creator.common.utils.ToastUtil
import com.creator.eternalbonds.databinding.ItemIpBinding


class IPAdapter(context: Context, val textView: TextView? = null) :
    BaseAdapter<ItemIpBinding>(context) {

    private val intranetIPAddress = "以下是内网ip地址"
    var pubIps: Set<String> = hashSetOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var priIps: Set<String> = hashSetOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var ip = when {
            position < pubIps.size -> {
                pubIps.toList()[position]
            }

            position == pubIps.size  || (pubIps.isEmpty() && position == 0) -> {
                intranetIPAddress
            }

            position >= pubIps.size -> {
                    priIps.toList()[position - pubIps.size-1]
            }

            else -> ""
        }


        if (pubIps.isEmpty() && priIps.isNotEmpty()) {
            textView?.text = "你没有公网ip地址，无法在非同一网络下的设备进行连接"
        } else if (pubIps.isNotEmpty()) {
            textView?.text = "以下是公网ip地址"
        }
        holder.itemView.setOnLongClickListener {
            if (ip != intranetIPAddress) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.text = ip
                ToastUtil.show(context, "复制成功:\n$ip")
            }
            true
        }

        holder.v.ipText.text = ip
    }

    override fun getItemCount(): Int {
        return pubIps.size + priIps.size + 1;
    }
}