package com.creator.common.bean.transmit

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.creator.common.bean.player.VideoPlayerDataBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * WebSocket信息传输类
 */
class WebSocketMessage<T>(message: T) {
    var message: T=message
        get() {
            return when (message) {
                //视频播放器相关数据类
                is VideoPlayerDataBean -> {
                    message
                }

                else -> {
                    message
                }
            }
        }
        set(value){
            field = value
        }


    override fun toString(): String {
        return Gson().toJson(this)
    }


    companion object{
        fun <T> toClass(str:String): WebSocketMessage<T> {
            val type: Type = object : TypeToken<WebSocketMessage<T>?>() {}.type
            return Gson().fromJson(str,type)

        }

    }




}