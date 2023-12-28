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
    }


    object Video{
       val MIME_TYPE= mapOf<Enums.VIDEO_TYPE,String>(
            Enums.VIDEO_TYPE.MKV to "video/x-matroska",
            Enums.VIDEO_TYPE.MP4 to "video/mp4",
        )
    }

}