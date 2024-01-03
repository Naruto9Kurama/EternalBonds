package com.creator.common.utils

import com.creator.common.Constants
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.CompletableFuture
import kotlin.experimental.and

object IPUtil {

    private const val TAG = "IPUtil"
    fun getIpAddress(block: ((ip: String) -> Unit)?) {
        Constants.IP.REQUEST_URL.forEach { url ->
            OkHttpClientUtil.asyncGet(url, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    kotlin.run {
                        var string = response.body.string()
                        LogUtil.d(TAG, string)

                        /*if (!isPublicIP(string)) {
                            string = ""
                        }*/
                        if (block != null) {
                            block(string)
                        }
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

    fun isPublicIP(ipAddress: String): Boolean {
        return isPublicIP(InetAddress.getByName(ipAddress))
    }

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

    fun isIpv6(ipAddress: String): Boolean {
        val future = CompletableFuture<Boolean>()
        Thread{
            try {
                when (InetAddress.getByName(ipAddress)) {
                    is Inet4Address -> {
                        future.complete( false)
                    }

                    is Inet6Address -> {
                        future.complete( true)
                    }
                    else->{
                        future.complete( false)
                    }
                }
            }catch (e:Exception){
                future.complete( false)
            }

        }.start()

        return future.get();
    }

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

    fun isPublicIPv6(ipAddress: String): Boolean {
        val inetAddress = InetAddress.getByName(ipAddress)

        return if (inetAddress is Inet6Address) {
            isPublicIPv6(inetAddress)
        } else {
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