package com.creator.common

import com.creator.common.enums.Enums
import com.creator.common.utils.IPUtil


data object Constants {

    object Data {
        object Ip {
            init {
                IPUtil.getIpAddress { allIps, pubIps, priIps ->
                    this.allIps = allIps
                    this.priIps = priIps
                    this.pubIps = pubIps
                    myIp = if (pubIps.isNotEmpty()) pubIps.toList()[0] else priIps.toList()[0]
                }
            }

            lateinit var allIps: Set<String>//全部ip
            lateinit var priIps: Set<String>//私有ip
            lateinit var pubIps: Set<String>//公网ip
            lateinit var myIp: String//我的ip 获取单个公网ip



        }
    }

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


    object Key {
        object Video {
            const val VideoServer = "VideoServer"
        }
    }


}