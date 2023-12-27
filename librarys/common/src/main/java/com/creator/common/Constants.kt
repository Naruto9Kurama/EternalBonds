package com.creator.common


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
            "https://ipinfo.io/ip",
            "http://icanhazip.com",
            "https://api.ipify.org",
            "http://httpbin.org/ip",
            "http://ip-api.com/json",
            "https://api64.ipify.org",
            "http://checkip.amazonaws.com"
        )
    }

}