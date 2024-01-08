package com.creator.common.utils

import com.creator.common.Constants
import com.creator.common.bean.PingResult
import com.creator.common.enums.Enums
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.CompletableFuture
import kotlin.experimental.and

object IPUtil {

    private const val TAG = "IPUtil"
    fun getIpAddress(
        block: ((ip: String, allIps: Set<String>, pubIps: Set<String>, priIps: Set<String>) -> Unit)? = null,
    ) {
        var ips = HashSet<String>()//公网ip
        var allIps = HashSet<String>()//全部ip
        var priIps = HashSet<String>()//私有ip
        Constants.IP.REQUEST_URL.forEachIndexed() { index, url ->
            LogUtil.d(TAG, "准备请求ip")
            OkHttpClientUtil.asyncGet(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    var ip = response.body.string().trim().replace("\\n$".toRegex(), "")
                    try {
                        LogUtil.d(TAG, ip)
                        if (isLocalIpAddress(ip)) {//ip是否是本机ip
                            allIps.add(ip)
                            if (velocity(ip)) {//测速是否成功
                                ips.add(ip)
                            } else {
                                priIps.add(ip)
                            }
                        }
                    } catch (e: Exception) {

                    } finally {
                        block?.invoke(ip, allIps, ips, priIps)
                    }
                }
            })
        }
    }

    fun getPublicIpAddress() {
        Constants.IP.REQUEST_URL.forEach { url ->
            OkHttpClientUtil.asyncGet(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    kotlin.run {
                        var string = response.body.string()
                        LogUtil.d(TAG, string)
                        if (isPublicIP(string)) {
                            LogUtil.d(TAG, string)
                        }
                    }
                }

            })
        }
    }


    /**
     * 判断获取到的ip是否是本机ip
     */
    fun isLocalIpAddress(ipAddress: String): Boolean {
        try {
            // 获取所有网络接口
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                // 获取每个网络接口的所有 IP 地址
                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    // 比较 IP 地址
                    val hostAddress = inetAddress.hostAddress

                    if (ipAddress == hostAddress) {
                        return true // IP 地址匹配，是本机 IP
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return false // 没有匹配的 IP 地址，不是本机 IP
    }

    /**
     * ip测速是否成功
     */
    fun velocity(ip: String, count: Int = 4): Boolean {

        var url = "${Constants.IP.VELOCITY_URL}/${
            if (isPublicIPv6(ip)) {
                Enums.IP.ipv6.name
            } else if (isPublicIPv4(ip)) {
                Enums.IP.ipv4.name
            } else {
                return false
            }
        }/${ip}/${count}/all"
        LogUtil.d(TAG, "请求的url: $url")
        val future = CompletableFuture<Boolean>()
        Thread {
            OkHttpClientUtil.asyncGet(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    future.complete(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val pingResult = PingResult.toClass(response.body.string())
                        future.complete(velocityIsSuccess(pingResult))
                    } catch (e: Exception) {
                        future.complete(false)
                    } finally {
                    }
                }
            })
        }.start()

        return future.get()
    }

    /**
     * 判断ip是否可ping成功
     */
    private fun velocityIsSuccess(pingResult: PingResult): Boolean {
        var isSuccess = false
        kotlin.runCatching {
            pingResult.pingResultDetail.forEach { pingResult ->
                isSuccess = pingResult.code.equals(Enums.PingResult.PingSuccess.name)
                if (isSuccess) {
                    return@runCatching
                }
            }
        }
        return isSuccess
    }

    /**
     * 判断是否是公共的ip地址
     */
    fun isPublicIP(ipAddress: String): Boolean {
        return isPublicIP(InetAddress.getByName(ipAddress))
    }

    /**
     * 判断是否是公共的ipv6地址
     */
    fun isPublicIP(ipAddress: InetAddress): Boolean {
        when (ipAddress) {
            is Inet4Address -> {
                return isPublicIPv4(ipAddress)
            }

            is Inet6Address -> {
                return isPublicIPv6(ipAddress)
            }
        }
        return false;
    }

    /**
     * 判断是否是ipv6地址
     */
    fun isIpv6(ipAddress: String): Boolean {
        val future = CompletableFuture<Boolean>()
        Thread {
            try {
                when (InetAddress.getByName(ipAddress)) {
                    is Inet4Address -> {
                        future.complete(false)
                    }

                    is Inet6Address -> {
                        future.complete(true)
                    }

                    else -> {
                        future.complete(false)
                    }
                }
            } catch (e: Exception) {
                future.complete(false)
            }

        }.start()

        return future.get();
    }

    /**
     * 判断是否是公共的ipv4地址
     */
    fun isPublicIPv4(ipAddress: Inet4Address): Boolean {
        return try {
            val addressBytes = ipAddress.address

            // Check if it's a private IPv4 address range
            !(addressBytes[0] == 10.toByte() || (addressBytes[0] == 172.toByte() && (addressBytes[1] in 16..31))
                    || (addressBytes[0] == 192.toByte() && addressBytes[1] == 168.toByte()))
        } catch (e: Exception) {
            LogUtil.e(TAG, e.message.toString(), e)
            false
        }
    }

    /**
     * 判断是否是公共的ipv4地址
     */
    fun isPublicIPv4(ipAddress: String): Boolean {
        val inetAddress = InetAddress.getByName(ipAddress)
        return if (inetAddress is Inet4Address) {
            isPublicIPv4(inetAddress)
        } else {
            false
        }
    }

    /**
     * 判断是否是有效的ip地址
     */
    fun ipIsReachable(ipAddress: String): Boolean {
        kotlin.runCatching {
            val future = CompletableFuture<Boolean>()
            Thread {
                future.complete(isHostReachable(ipAddress, Constants.WebSocket.PORT))
            }.start()
            return future.get()
            /*val inetAddress = InetAddress.getByName(ipAddress)
            return ipIsReachable(inetAddress);*/
        }
        return false
    }

    fun ipIsReachable(inetAddress: InetAddress): Boolean {

        return inetAddress.isReachable(3000);
    }

    fun isPublicIPv6(ipAddress: String): Boolean {
        val inetAddress = InetAddress.getByName(ipAddress)
        return if (inetAddress is Inet6Address) {
            isPublicIPv6(inetAddress)
        } else {
            false
        }
    }

    /**
     * 测试地址是否可达
     */
    fun isHostReachable(host: String, port: Int, timeout: Int = 3000): Boolean {
        return try {
            val socket = Socket()
            socket.connect(java.net.InetSocketAddress(host, port), timeout)
            socket.close()
            true
        } catch (e: Exception) {
            LogUtil.d(TAG,"网络不可达")
            false
        }
    }

    fun isPublicIPv6(ipAddress: Inet6Address): Boolean {
        return try {
            val addressBytes = ipAddress.address
            !(addressBytes[0] == 0xfc.toByte() && (addressBytes[1] and 0xfe.toByte() == 0x80.toByte()))
        } catch (e: Exception) {
            LogUtil.e(TAG, e.message.toString(), e)
            false
        }
    }


    fun isIPv6Supported(): Boolean {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()

            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val addresses = networkInterface.inetAddresses

                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()

                    if (address is Inet6Address) {
                        // 如果发现IPv6地址，说明设备支持IPv6
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 如果没有发现IPv6地址，说明设备不支持IPv6
        return false
    }

}