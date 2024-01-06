package com.creator.eternalbonds

import com.creator.common.bean.PingResult
import com.creator.common.utils.IPUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.OkHttpClientUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.net.InetAddress


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
// 获取 InetAddress 对象
        // 获取 InetAddress 对象
        val inetAddress = InetAddress.getByName("240e:379:20a4:3c10:a054:b2d0:6d3e:bf9a")
        val reachable = inetAddress.isReachable(2000)
        LogUtil.d("ExampleUnitTest",reachable.toString())
    }

    @Test
    fun ipTest(){

        OkHttpClientUtil.asyncGet("https://ipw.cn/api/ping/ipv6/2409:8a34:2072:5310:acee:6283:5205:7f56/4/all",object :
            Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                kotlin.run {
                    var string = response.body.string()
                    val fromJson = Gson().fromJson(string, PingResult::class.java)
                    val message = fromJson.pingResultDetail[0].message
                    println(message)
                }
            }

        })
        while (true){
            
        }
    }
}