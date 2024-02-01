package com.creator.common.bean.transmit

import com.creator.common.bean.player.VideoPlayerDataBean


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




}