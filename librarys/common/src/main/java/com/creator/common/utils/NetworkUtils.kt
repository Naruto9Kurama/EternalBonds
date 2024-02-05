package com.creator.common.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast


object NetworkUtils {
    private var  networkChangeListeners= arrayListOf<NetworkChangeListener>()

    /**
     * 网络状态改变
     */
    interface NetworkChangeListener{
        fun onConnect()//已连接网络

        fun onDisconnection()//断开网络
    }
    fun registerNetworkChangeListener(networkChangeListener: NetworkChangeListener){
        networkChangeListeners.add(networkChangeListener)
    }
    class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable(context!!)) {
                // 网络已连接
                Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show()
                //获取ip地址
                IPUtil.setIp()
                networkChangeListeners.forEach {
                    it.onConnect()
                }
            } else {
                // 网络已断开
                Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show()
                networkChangeListeners.forEach {
                    it.onDisconnection()
                }
            }
        }

    }
    private val networkChangeReceiver = NetworkChangeReceiver()

     fun registerNetworkChangeReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
         context.registerReceiver(networkChangeReceiver, intentFilter)
    }

     fun unregisterNetworkChangeReceiver(context: Context) {
         context.unregisterReceiver(networkChangeReceiver)
    }
    /**
     * 检测当前设备是否可联网
     */
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    // for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    // for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }

        return false
    }
}
