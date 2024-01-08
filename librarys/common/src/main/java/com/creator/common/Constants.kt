package com.creator.common

import com.creator.common.enums.Enums


data object Constants {

    /**
     * WebSocket Constants
     */
    object WebSocket {
        const val PORT = 8089
        const val URL = ""
    }

    object NanoHttpd {
        const val PORT = 8088
    }

    object IP {
        val REQUEST_URL = arrayListOf<String>(
            //ipv6
            "https://api64.ipify.org",
            "http://ipv6-test.com/api/myip.php",
            "http://icanhazip.com",
            "https://api.ipify.org",
            //ipv4
//            "http://checkip.amazonaws.com",//包括地区位置
//            "http://httpbin.org/ip",
//            "http://ip-api.com/json",
            "https://ipinfo.io/ip"
        )
        const val VELOCITY_URL = "https://ipw.cn/api/ping"
        const val IPV4 = "ipv4"
        const val IPV6 = "ipv6"
    }


    object Video {
        val MIME_TYPE = mapOf<Enums.VideoType, String>(
            Enums.VideoType.MKV to "video/x-matroska",
            Enums.VideoType.MP4 to "video/mp4",
        )
    }



}